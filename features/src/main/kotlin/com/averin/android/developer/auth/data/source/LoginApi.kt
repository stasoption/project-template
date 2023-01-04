package com.averin.android.developer.auth.data.source

import com.averin.android.developer.auth.domain.model.UserResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

interface LoginApi {
    @GET("user")
    suspend fun login(@Header("Authorization") basicAuthHeader: String): UserResponse
    @DELETE("auth/")
    suspend fun logout(): Response<Unit>


}
