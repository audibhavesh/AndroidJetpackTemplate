package com.theoctacoder.cats_facts.network

import com.fermion.android.base.network.api.BaseApiService
import com.theoctacoder.cats_facts.ui.facts.models.CatFactModel
import retrofit2.Response
import retrofit2.http.GET

interface CatFactService : BaseApiService {

    @GET("fact")
    suspend fun getFact(): Response<CatFactModel>
}