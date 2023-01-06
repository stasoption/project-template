package com.averin.android.developer.auth.data

import android.content.SharedPreferences
import com.averin.android.developer.auth.data.source.LoginApi
import com.averin.android.developer.auth.domain.LoginRepository
import com.averin.android.developer.auth.domain.model.UserResponse
import com.averin.android.developer.base.util.FlowPreferences
import com.averin.android.developer.base.util.FlowPreferences.Companion.ARG_GITHUB_USER_NAME
import com.averin.android.developer.core.BuildConfig.PREFERENCES_VERSION
import com.squareup.moshi.Moshi

class LoginRepositoryImpl(
    private val loginApi: LoginApi,
    moshi: Moshi,
    sharedPreferences: SharedPreferences
) : LoginRepository {

    private val prefs = FlowPreferences(
        moshi = moshi,
        sharedPreferences = sharedPreferences,
        version = PREFERENCES_VERSION,
        fallbackToDestructiveMigration = true
    )

    override var gitHubUserName by prefs[ARG_GITHUB_USER_NAME, String::class]

    override suspend fun loadProfile(username: String): UserResponse {
        return loginApi.loadProfile(username.trim())
    }
}
