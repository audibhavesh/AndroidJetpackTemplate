package com.fermion.android.base.models.network

/**
 * Created by Bhavesh Auodichya.
 *
 * Http error response class
 *
 *
 *@since 1.0.0
 */

data class HttpErrorResponse(
    var code: Int,
    var message: String,
    var errorSource: String? = null,
    var errorDescription: String? = null,
    var httpResponse: Any? = null,
    var extra: String? = null,

    )
