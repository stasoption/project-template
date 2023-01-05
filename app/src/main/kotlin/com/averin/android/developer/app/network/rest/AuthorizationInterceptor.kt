package com.averin.android.developer.app.network.rest

import android.content.SharedPreferences
import com.averin.android.developer.base.util.FlowPreferences
import com.averin.android.developer.core.BuildConfig.KEY_AUTH_LOGIN_TOKEN
import com.averin.android.developer.core.BuildConfig.PREFERENCES_VERSION
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    moshi: Moshi,
    sharedPreferences: SharedPreferences
) : Interceptor {

    private val prefs = FlowPreferences(
        moshi = moshi,
        sharedPreferences = sharedPreferences,
        version = PREFERENCES_VERSION,
        fallbackToDestructiveMigration = true
    )

    private var loginToken by prefs[KEY_AUTH_LOGIN_TOKEN, String::class]

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        loginToken?.let { token ->
            requestBuilder.header(AUTHORIZATION_HEADER, "$AUTHORIZATION_BEARER $token")
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val AUTHORIZATION_BEARER = "Bearer"
    }
}
