package com.averin.android.developer.settings.presentation

import com.averin.android.developer.base.util.FlowPreferences
import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.settings.domain.SettingsInteractor
import com.averin.android.developer.settings.domain.model.UserInfo

class MenuViewModel(
    val interactor: SettingsInteractor,
    val flowPreferences: FlowPreferences
) : BaseViewModel() {

    val getUserInfoEvent = SingleLiveEvent<UserInfo>()
    val logoutSuccessEvent = SingleLiveEvent<String?>()

    fun getUserInfo() = launchLoadingErrorJob {
//        getUserInfoEvent.value = interactor.getUserInfo()
    }

    fun logout() = launchLoadingErrorJob {
        val response = interactor.logout()
//        if (response.code() == LOGOUT_CODE) {
//            flowPreferences.clear()
//            interactor.loginToken = null
//            logoutSuccessEvent.value = null
//        } else {
//            val error = response.errorBody()?.string()
//            logoutSuccessEvent.value = error ?: ""
//        }
    }
}
