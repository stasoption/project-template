package com.averin.android.developer.auth.presentation

import com.averin.android.developer.auth.domain.LoginInteractor
import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel
import kotlinx.coroutines.Job

class LoginViewModel(private val loginInteractor: LoginInteractor) : BaseViewModel() {

    var email: String = ""
    var password: String = ""

    val loginSuccessEvent = SingleLiveEvent<Nothing>()
    private var loginJob: Job? = null

    fun login() {
        if (loginJob?.isCompleted != false) {
            loginJob = launchLoadingErrorJob {
                loginInteractor.login(email, password).run {
                    loginInteractor.loginToken = ""
                    loginSuccessEvent.call()
                }
            }
        }
    }
}
