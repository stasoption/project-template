package com.averin.android.developer.auth.presentation

import com.averin.android.developer.auth.domain.LoginInteractor
import com.averin.android.developer.auth.domain.model.UserResponse
import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel

class LoginViewModel(private val loginInteractor: LoginInteractor) : BaseViewModel() {

    var email: String = ""
    val userResponseLiveData = SingleLiveEvent<UserResponse>()

    fun loadProfile() = launchLoadingErrorJob {
        val userResponse = loginInteractor.loadProfile(email)
        loginInteractor.gitHubUserName = userResponse.login
        userResponseLiveData.value = userResponse
    }
}
