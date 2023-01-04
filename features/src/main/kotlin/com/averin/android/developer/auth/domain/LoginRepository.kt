package com.averin.android.developer.auth.domain

import com.averin.android.developer.auth.domain.model.UserResponse
import retrofit2.Response

interface LoginRepository {
    var loginToken: String?
    suspend fun login(username: String, password: String): UserResponse
    suspend fun logout(): Response<Unit>
}
