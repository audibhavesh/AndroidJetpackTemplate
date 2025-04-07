package com.fermion.android.base.di


import androidx.annotation.NonNull
import com.fermion.android.base.config.Environment
import com.fermion.android.base.config.NetworkConfig
import com.fermion.android.base.config.RunConfig
import com.fermion.android.base.network.api.BaseApiService
import com.fermion.android.base.network.NetworkFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.Reader
import java.util.ServiceLoader
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLPeerUnverifiedException
import kotlin.coroutines.CoroutineContext

/**
 * Created by Bhavesh Auodichya.
 *
 * BaseNetworkModule injects necessary network components.
 *
 *
 *@since 1.0.0
 */

@Module
@InstallIn(SingletonComponent::class)
class BaseNetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        for (factory in ServiceLoader.load(
            TypeAdapterFactory::
            class.java
        )) {
            builder.registerTypeAdapterFactory(factory)
        }
        return builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setLenient().create()
    }

    @Provides
    @Singleton
    fun provideRunConfig(): RunConfig {
        return RunConfig()
    }

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return IO
    }

    @Provides
    @Singleton
    fun provideFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideCertificatePinner(runConfig: RunConfig): CertificatePinner {
        val url = runConfig.baseUrl.replace("https:", "")
            .replace("/", "")
        return CertificatePinner.Builder()
            .add(url, "sha256/" + runConfig.sslPinnerSha256).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        certificatePinner: CertificatePinner,
        runConfig: RunConfig
    ): OkHttpClient {
        val okHttpBuilder =
            OkHttpClient.Builder() // must be the last interceptor to catch and log modified requests
        val loggingInterceptor =
            HttpLoggingInterceptor()
        loggingInterceptor.level =
            HttpLoggingInterceptor.Level.BODY
        if (runConfig.sslPinnerSha256.isNotBlank()) {
            okHttpBuilder.certificatePinner(
                certificatePinner
            )
        }
        okHttpBuilder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + NetworkConfig.accessToken).build()
            try {
                val hasMultipart = request.headers.names().contains("multipart")
                loggingInterceptor.setLevel(if (hasMultipart) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.BODY)
                val response = chain.proceed(request)
//                if (runConfig.environment == Environment.DEV) {
//
//                }

                response
            } catch (sslPeerUnverifiedException: SSLPeerUnverifiedException) {
                Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(400)
                    .message("Can't connect to unverified server")
                    .body(
                        ("Cannot verify server identity\n Please contact IT support").toResponseBody(
                            "".toMediaTypeOrNull()
                        )
                    )
                    .build()
            } catch (e: Exception) {
                println(e)
                Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(404)
                    .message("Connection Timeout").body(
                        (e.localizedMessage
                            ?: "Exception thrown from network with 404 status code").toResponseBody(
                            "".toMediaTypeOrNull()
                        )
                    ).build()
            }
        })
        if (runConfig.environment != Environment.PROD) {
            okHttpBuilder.addNetworkInterceptor(loggingInterceptor)
        }
        return okHttpBuilder.callTimeout(NetworkConfig.apiCallTimeout, TimeUnit.MINUTES)
            .writeTimeout(NetworkConfig.apiWriteTimeout, TimeUnit.MINUTES)
            .connectTimeout(NetworkConfig.apiConnectionTimeout, TimeUnit.MINUTES)
            .readTimeout(NetworkConfig.apiReadTimeout, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Throws(IOException::class)
    fun requestBodyToString(request: RequestBody): String? {
        return try {
            val buffer = Buffer()
            request.writeTo(buffer)
            val requestString = buffer.readUtf8()
            buffer.close()
            requestString
        } catch (e: IOException) {
            ""
        }
    }

    fun responseBodyToString(`in`: Reader): String? {
        return try {
            val bufferSize = 1024
            val buffer = CharArray(bufferSize)
            val out = StringBuilder()
            var numRead: Int
            while (`in`.read(buffer, 0, buffer.size).also { numRead = it } > 0) {
                out.appendRange(buffer, 0, numRead)
            }
            out.toString()
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    @Provides
    @NonNull
    fun provideBaseApiService(
        @NonNull serviceFactory: NetworkFactory,
        runConfig: RunConfig
    ): BaseApiService {
        return serviceFactory.create(
            runConfig.baseUrl, BaseApiService::class.java
        )
    }
}