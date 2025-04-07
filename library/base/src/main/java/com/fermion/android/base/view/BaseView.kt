package com.fermion.android.base.view

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fermion.android.base.constants.ProgressState

/**
 * Created by Bhavesh Auodichya.
 *
 * **Note** : this class provides  basic functionality to your custom Screen like loader and navigation.
 *
 * Extend this class with your Screen
 *
 * While extending add composable in Bind for rendering you screen
 *
 *
 * @property viewModel use this for handling all viewmodel functionality
 * @property bundle use this for getting arguments passed by previous screen
 * @property extra  use this if any extra data passed or need to be stored.
 *
 **
 *@since 1.0.0
 */

abstract class BaseView<V : BaseViewModel> {

    lateinit var viewModel: V

    var bundle: Bundle? = null
    var extra: Any? = null

    @Composable
    protected abstract fun provideViewModel(navHostController: NavHostController?): V

    @Composable
    protected abstract fun Bind(navHostController: NavHostController): Unit

    @Composable
    protected open fun Bind() {
    }

    @Composable
    fun Init(
        navHostController: NavHostController?,
        context: Context,
        bundle: Bundle?,
        extra: Any? = null
    ) {
        this.bundle = bundle
        this.extra = extra
        viewModel = provideViewModel(navHostController)
        Box(modifier = Modifier.fillMaxSize()) {
            Bind(navHostController ?: rememberNavController())
            if (::viewModel.isInitialized) {
                when (viewModel.showProgress.collectAsState().value) {
                    ProgressState.HIDE -> {
                        (context as BaseActivity).hideProgressBar()
                    }

                    ProgressState.SHOW -> {
                        (context as BaseActivity).showProgressBar()
                    }
                }
            }
        }
    }
}