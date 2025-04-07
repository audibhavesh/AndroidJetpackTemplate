package com.fermion.android.base.view

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import com.fermion.android.base.helper.BaseProgressBarProvider
import com.fermion.android.base.util.LoaderUtil


/**
 * Created by Bhavesh Auodichya.
 *
 * **Note** : this class provides  basic functionality to your Activity like loader handling.
 *
 * if you want to implement your own custom loader implement BaseProgressBarProvider in your activity
 *
 **
 *@since 1.0.0
 */

abstract class BaseActivity(var customLoader: Boolean) : ComponentActivity(),
    BaseProgressBarProvider {
    constructor() : this(false)

    @Composable
    override fun showProgressBar() {
        if (!customLoader) {
            LoaderUtil.ShowLoader()
        }
    }

    @Composable
    override fun hideProgressBar() {
    }

}