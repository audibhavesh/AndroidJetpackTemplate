package com.fermion.android.base.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by Bhavesh Auodichya.
 *
 * **Info** : Base Data Source provide result wrapper for network  or other common SST
 *
 *@since 1.0.0
 */
abstract class BaseDataSource {
    protected suspend fun <T> makeNetworkCall(networkCall: suspend () -> Response<T>): Flow<NetworkResult<T>> =
        flow {
            try {
                emit(NetworkResult.Loading)
                val response = networkCall()
                emit(NetworkResult.Success(response.body()))
            } catch (httpException: HttpException) {
                emit(NetworkResult.HttpError(httpException))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e))
            }
        }

    suspend fun <T> makeNetworkCallAndSaveResponse(
        networkCall: suspend () -> T, saveCallResult: suspend (T) -> Unit = {}
    ): Flow<NetworkResult<T>> = flow {
        emit(NetworkResult.Loading)
        try {
            val responseStatus = networkCall.invoke()
            emit(NetworkResult.Success(responseStatus))
            saveCallResult.invoke(responseStatus)
        } catch (e: HttpException) {
            emit(NetworkResult.HttpError(e))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e))
        }

    }

    suspend fun <T> makeNetworkCallWithLocalSource(
        networkCall: suspend () -> T,
        localSourceResultCall: suspend () -> T,
        saveCallResult: suspend (T) -> Unit
    ): Flow<NetworkResult<T>> = flow {
        emit(NetworkResult.Loading)
        try {
            val responseStatus = networkCall.invoke()
            saveCallResult(responseStatus)
            emit(NetworkResult.Success(responseStatus))
        } catch (e: HttpException) {
            try {
                val localSourceResultResponse = localSourceResultCall.invoke()
                emit(NetworkResult.Success(localSourceResultResponse))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e))
            }
        } catch (e: Exception) {
            try {
                val localSourceResultResponse = localSourceResultCall.invoke()
                emit(NetworkResult.Success(localSourceResultResponse))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e))
            }
        }
    }


}