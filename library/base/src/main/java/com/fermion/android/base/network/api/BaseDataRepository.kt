package com.fermion.android.base.network.api

import com.fermion.android.base.network.BaseDataSource
import javax.inject.Inject


/**
 * Created by Bhavesh Auodichya.
 *
 *BaseDataRepository
 *
 * **Note** Extend this class to your repository
 *
 * **Info** : Implement common  apis or SST Strategy here
 *
 * @property apiService use this for calling base apis
 *
 *@since 1.0.0
 */
open class BaseDataRepository @Inject constructor(private val apiService: BaseApiService) :
    BaseDataSource() {
    @Suppress("unused")
    suspend fun testDomain() = makeNetworkCall(networkCall = { apiService.testDomain() })
}