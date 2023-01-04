package com.averin.android.developer.baseui.error

interface ErrorView {
    fun showNetworkError(endpoint: String?, t: Throwable)
    fun showUnexpectedError(t: Throwable)
    fun showHttpException(t: Throwable)
}
