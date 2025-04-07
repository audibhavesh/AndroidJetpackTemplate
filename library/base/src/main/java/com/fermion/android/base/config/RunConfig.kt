package com.fermion.android.base.config

import com.fermion.android.base.BuildConfig


/**
 * Created by Bhavesh Auodichya.
 *
 * RunConfig Class
 *
 * Inherit this class and add your custom run time configurations
 * This contain global environment and base url
 * @property environment contains current environment set in environmet.gradle (DEV, PROD etc)
 * @property baseUrl contains base url set in environment.gradle
 *@since 1.0.0
 */
open class RunConfig {
    val environment: Environment = BuildConfig.ENVIRONMENT
    val baseUrl = BuildConfig.BASE_API_URL
    val sslPinnerSha256=BuildConfig.sslPinnerSha256
}