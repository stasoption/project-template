package com.averin.android.developer.baseui.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    open val error = MutableLiveData<Throwable?>()
    open val loading = MutableLiveData<Boolean>()

    private fun createExceptionHandler(
        error: MutableLiveData<Throwable?>?,
        exceptionBlock: ((Throwable) -> Unit)? = null
    ): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            exceptionBlock?.invoke(throwable)
            error?.postValue(throwable)
        }
    }

    protected fun launchJob(
        error: MutableLiveData<Throwable?>? = this.error,
        loading: MutableLiveData<Boolean>? = this.loading,
        onError: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ): Job {
        return viewModelScope.launch(createExceptionHandler(error, onError)) {
            try {
                error?.postValue(null)
                loading?.postValue(true)
                block()
            } finally {
                loading?.postValue(false)
            }
        }
    }

    protected fun <T> deferredAsync(
        error: MutableLiveData<Throwable?>? = this.error,
        onError: ((Throwable) -> Unit)? = null,
        block: suspend () -> T
    ): Deferred<T> {
        return viewModelScope.async(createExceptionHandler(error, onError) + Dispatchers.IO) {
            error?.postValue(null)
            block.invoke()
        }
    }
}
