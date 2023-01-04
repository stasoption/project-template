package com.averin.android.developer.baseui.error

import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GlobalErrorHandler(
    private val errorView: ErrorView
) {
    fun handleError(t: Throwable?) {
        t?.let { onError(it) }
    }

    private fun onError(t: Throwable) {
        Timber.e(t)
        when {
            t is KotlinNullPointerException -> handleKotlinNullPointerException(t)
            t is JsonDataException -> errorView.showUnexpectedError(t)
            t is HttpException -> handleHttpException(t)
            NETWORK_EXCEPTIONS.contains(t.javaClass) -> { errorView.showNetworkError(null, t) }
            else -> { errorView.showUnexpectedError(t) }
        }
    }

    private fun handleKotlinNullPointerException(t: Throwable) {
        errorView.showUnexpectedError(t)
    }

    private fun handleHttpException(e: HttpException) {
        try {
            // TODO: Process error from a server here
            errorView.showHttpException(e)
        } catch (e1: Exception) {
            Timber.e(e1)
            errorView.showUnexpectedError(e1)
        }
    }

    companion object {
        const val HEADER_REQUEST_ID = "x-request-id"
        private val NETWORK_EXCEPTIONS = listOf<Class<*>>(
            UnknownHostException::class.java,
            SocketTimeoutException::class.java,
            ConnectException::class.java
        )
    }
}
