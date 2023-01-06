package com.averin.android.developer.auth.domain

import com.averin.android.developer.auth.domain.model.UserResponse

interface LoginRepository {
    var gitHubUserName: String?
    suspend fun loadProfile(username: String): UserResponse
}
