package com.averin.android.developer.settings.domain

import com.averin.android.developer.auth.domain.LoginRepository

class SettingsInteractor(
    private val settingsRepository: SettingsRepository,
    private val loginRepository: LoginRepository,
) {
    var loginToken: String?
        get() = loginRepository.loginToken
        set(loginToken) {
            loginRepository.loginToken = loginToken
        }
    suspend fun logout() = loginRepository.logout()
}
