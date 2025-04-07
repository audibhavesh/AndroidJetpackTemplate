package com.theoctacoder.cats_facts.network

import androidx.annotation.NonNull
import com.fermion.android.base.network.NetworkFactory
import com.theoctacoder.cats_facts.config.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class CatNetworkModule {

    private var appConfig: AppConfig = AppConfig()

    @Provides
    @NonNull
    fun provideCatFactApiService(
        @NonNull serviceFactory: NetworkFactory
    ): CatFactService {
        return serviceFactory.create(
            appConfig.baseUrl, CatFactService::class.java
        )
    }
}