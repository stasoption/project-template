package com.averin.android.developer.settings.domain

import com.averin.android.developer.auth.domain.model.UserResponse

class SettingsInteractor(
    private val settingsRepository: SettingsRepository
) {
    var githubUserName: String?
        get() = settingsRepository.githubUserName
        set(userName) {
            settingsRepository.githubUserName = userName
        }

    suspend fun loadProfile(): UserResponse? = settingsRepository.loadProfile()
}
