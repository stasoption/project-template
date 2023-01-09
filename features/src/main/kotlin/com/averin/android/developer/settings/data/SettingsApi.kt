package com.averin.android.developer.settings.data

import com.averin.android.developer.auth.domain.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SettingsApi {
    @GET("users/{username}")
    suspend fun loadProfile(@Path("username") username: String): UserResponse?
}
