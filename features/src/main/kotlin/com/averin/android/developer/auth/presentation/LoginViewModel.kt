package com.averin.android.developer.auth.presentation

import com.averin.android.developer.auth.domain.LoginInteractor
import com.averin.android.developer.auth.domain.model.UserResponse
import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel

class LoginViewModel(private val loginInteractor: LoginInteractor) : BaseViewModel() {

    var email: String = ""
    val userResponseLiveData = SingleLiveEvent<UserResponse>()
    val loginWithoutMyUserNameEvent = SingleLiveEvent<Nothing>()

    fun loadProfile() = launchJob {
        val userResponse = loginInteractor.loadProfile(email)
        loginInteractor.gitHubUserName = userResponse.login
        userResponseLiveData.value = userResponse
    }

    fun loginWithMyPersonalUserName() {
        loginInteractor.gitHubUserName = MY_USER_NAME
        loginWithoutMyUserNameEvent.call()
    }

    companion object {
        private const val MY_USER_NAME = "stasoption"
    }
}
