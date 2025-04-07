package com.fermion.android.base.helper

import androidx.compose.runtime.Composable


/**
 * Created by Bhavesh Auodichya.
 *
 * Interface for implementing custom progress bar provider
 *
 * **Note** extend this interface in your view class to implement custom progress bar.
 *
 *
 *@since 1.0.0
 */
interface BaseProgressBarProvider {
    @Composable fun showProgressBar()
    @Composable fun hideProgressBar()
}