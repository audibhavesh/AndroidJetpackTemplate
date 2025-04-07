package com.fermion.android.base.network.api

import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Bhavesh Auodichya.
 *
 * **Note** Extend this class to your api service.
 *
 * **Info** : Include apis which are  common to all modules
 *
 *@since 1.0.0
 */
interface BaseApiService {

    /**
     * Created by Bhavesh Auodichya.
     *
     *
     * Api to test url domain status
     *@since 1.0.0
     */
    @GET
    fun testDomain(): Response<Nothing>

}