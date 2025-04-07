package com.fermion.android.base.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fermion.android.base.constants.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * Created by Bhavesh Auodichya.
 *
 * BaseViewModel extends ViewModel
 *
 * **Note** : this class provides  view model with basic functionality to your custom view model.
 *
 * Extend this class with your view model for dagger injection
 *
 * **Info** : While defining your view model annotate it with HiltViewModel to
 * generate view-model injection code
 *
 * @property showProgress use this observable to handle progress eg (api, data processing etc)
 * @property showError use this observable to handle error
 **
 *@since 1.0.0
 */

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {
    val showProgress: MutableStateFlow<ProgressState> = MutableStateFlow(ProgressState.HIDE)
    val showError: MutableStateFlow<Throwable> = MutableStateFlow(Throwable())

}