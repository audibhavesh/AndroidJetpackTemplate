package com.theoctacoder.cats_facts.network

import com.fermion.android.base.network.NetworkResult
import com.fermion.android.base.network.api.BaseDataRepository
import com.theoctacoder.cats_facts.ui.facts.models.CatFactModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatFactRepository @Inject constructor(private val catFactService: CatFactService) :
    BaseDataRepository(catFactService) {

    suspend fun getFact(): Flow<NetworkResult<CatFactModel>> {
        return makeNetworkCall {
            catFactService.getFact()
        }
    }

}