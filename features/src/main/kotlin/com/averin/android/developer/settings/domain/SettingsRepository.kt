package com.averin.android.developer.settings.domain

import com.averin.android.developer.auth.domain.model.UserResponse

interface SettingsRepository {
    var githubUserName: String?
    suspend fun loadProfile(): UserResponse?
}
