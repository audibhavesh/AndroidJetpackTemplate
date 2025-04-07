package com.fermion.android.base.network

import com.fermion.android.base.helper.JsonHelper
import com.fermion.android.base.models.network.HttpErrorResponse
import retrofit2.HttpException

/**
 * Created by Bhavesh Auodichya.
 *
 * **Note** Network Result class provides success, http error, error, and loading data events
 *
 *@since 1.0.0
 */
sealed class NetworkResult<out T> {

    data class Success<out T>(val data: T?) : NetworkResult<T>()
    data class HttpError<out T>(val exception: HttpException) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

}

fun <T> HttpException.getExceptionResponse(): HttpErrorResponse {
    val code = this.code()
    val message = if (this.message().isNotEmpty()) "\n" + this.message else ""

    try {
        val responseStream = response()?.errorBody()!!.charStream().readText()
        return if (JsonHelper().isJsonValid(responseStream)) {

            /*
            * Here HttpErrorResponse class is consider standard http error response sent from server
            * If your backend api throws different Http exception response
            * then please change it with your respective response model
            *
            * */

            return HttpErrorResponse(
                code,
                message,
                errorSource = "HTTP",
                errorDescription = null,
                httpResponse = responseStream,

                )
        } else {
            return HttpErrorResponse(
                code,
                message = "Server Code $code ${message}\n Please try again",
                errorSource = "HTTP",
                errorDescription = null,
                httpResponse = responseStream,
            )
        }
    } catch (e: Exception) {
        return HttpErrorResponse(
            code,
            message = "Server Code $code ${message}\n Please try again",
            errorSource = "HTTP",
            errorDescription = null,
        )
    }
}