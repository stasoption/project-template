package com.averin.android.developer.settings.presentation

import com.averin.android.developer.auth.domain.model.UserResponse
import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.settings.domain.SettingsInteractor

class MenuViewModel(
    val interactor: SettingsInteractor
) : BaseViewModel() {

    val getUserInfoEvent = SingleLiveEvent<UserResponse?>()
    val logoutSuccessEvent = SingleLiveEvent<Boolean>()

    fun getUserInfo() = launchJob {
        getUserInfoEvent.value = interactor.loadProfile()
    }

    fun logout() = launchJob {
        interactor.githubUserName = null
        logoutSuccessEvent.value = interactor.githubUserName == null
    }
}
