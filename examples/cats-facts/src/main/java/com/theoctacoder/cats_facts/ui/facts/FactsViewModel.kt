package com.theoctacoder.cats_facts.ui.facts

import androidx.lifecycle.viewModelScope
import com.fermion.android.base.constants.ProgressState
import com.fermion.android.base.network.NetworkResult
import com.fermion.android.base.view.BaseViewModel
import com.theoctacoder.cats_facts.ui.facts.models.CatFactModel
import com.theoctacoder.cats_facts.network.CatFactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FactsViewModel @Inject constructor(private val catFactRepository: CatFactRepository) :
    BaseViewModel() {
    var callOnce = false
    val catFactListener = MutableStateFlow<CatFactModel?>(CatFactModel(fact = "", length = 0))

    fun getCatFact() {
        viewModelScope.launch {
            callOnce = true
            try {
                catFactRepository.getFact().collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            delay(2000)
                            showProgress.emit(ProgressState.HIDE)
                            catFactListener.emit(it.data)
                        }

                        is NetworkResult.Error -> {
                            showProgress.emit(ProgressState.HIDE)

                        }

                        is NetworkResult.HttpError -> {
                            showProgress.emit(ProgressState.HIDE)
                        }

                        NetworkResult.Loading -> {
                            showProgress.emit(ProgressState.SHOW)
                        }
                    }
                }
            } catch (e: Exception) {
                showProgress.emit(ProgressState.HIDE)
                Timber.e(e)

            }
        }
    }
}