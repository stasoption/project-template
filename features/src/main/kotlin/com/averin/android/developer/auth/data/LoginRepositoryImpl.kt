package com.averin.android.developer.auth.data

import android.content.SharedPreferences
import com.averin.android.developer.auth.data.source.LoginApi
import com.averin.android.developer.auth.domain.LoginRepository
import com.averin.android.developer.auth.domain.model.UserResponse
import com.averin.android.developer.base.util.FlowPreferences
import com.averin.android.developer.core.BuildConfig.KEY_AUTH_LOGIN_TOKEN
import com.averin.android.developer.core.BuildConfig.PREFERENCES_VERSION
import com.squareup.moshi.Moshi
import okhttp3.Credentials

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

    override var loginToken by prefs[KEY_AUTH_LOGIN_TOKEN, String::class]

    override suspend fun login(username: String, password: String): UserResponse {
        return loginApi.login(Credentials.basic(username, password))
    }
    override suspend fun logout() = loginApi.logout()
}
