package com.fermion.android.base.view.theme

import androidx.lifecycle.viewModelScope
import com.fermion.android.base.constants.AppThemeType
import com.fermion.android.base.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : BaseViewModel() {
    var appTheme = MutableStateFlow(ThemePreference.getTheme())
    var showThemeDialog = MutableStateFlow(false)

    fun setTheme(newAppTheme: AppThemeType) {
        viewModelScope.launch {
            ThemePreference.setTheme(newAppTheme)
            appTheme.emit(newAppTheme)
        }
    }

    fun showDialog(shouldShow: Boolean) {
        viewModelScope.launch {
            showThemeDialog.emit(shouldShow)
        }
    }
}