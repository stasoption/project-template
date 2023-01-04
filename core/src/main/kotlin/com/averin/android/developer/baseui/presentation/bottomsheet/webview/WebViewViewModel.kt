package com.averin.android.developer.baseui.presentation.bottomsheet.webview

import android.content.SharedPreferences
import com.averin.android.developer.base.util.FlowPreferences
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.core.BuildConfig
import com.squareup.moshi.Moshi

class WebViewViewModel(moshi: Moshi, sharedPreferences: SharedPreferences) : BaseViewModel() {
    private val prefs = FlowPreferences(
        moshi = moshi,
        sharedPreferences = sharedPreferences,
        version = BuildConfig.PREFERENCES_VERSION,
        fallbackToDestructiveMigration = true
    )
    val loginToken by prefs[BuildConfig.KEY_AUTH_LOGIN_TOKEN, String::class]
}
