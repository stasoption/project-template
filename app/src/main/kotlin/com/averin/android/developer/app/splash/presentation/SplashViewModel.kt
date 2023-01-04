package com.averin.android.developer.app.splash.presentation

import com.averin.android.developer.auth.domain.LoginInteractor
import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel

class SplashViewModel(private val loginInteractor: LoginInteractor) : BaseViewModel() {
    val navigateToNextActivity = SingleLiveEvent<Boolean>()

    fun timerFinish() {
        navigateToNextActivity.value = !loginInteractor.loginToken.isNullOrEmpty()
    }
}
