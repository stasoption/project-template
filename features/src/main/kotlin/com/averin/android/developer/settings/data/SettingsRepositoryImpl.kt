package com.averin.android.developer.settings.data

import android.content.SharedPreferences
import com.averin.android.developer.base.util.FlowPreferences
import com.averin.android.developer.base.util.FlowPreferences.Companion.ARG_USER_EMAIL
import com.averin.android.developer.core.BuildConfig
import com.averin.android.developer.settings.domain.SettingsRepository
import com.squareup.moshi.Moshi

class SettingsRepositoryImpl(
    private val settingsApi: SettingsApi,
    moshi: Moshi,
    sharedPreferences: SharedPreferences
) : SettingsRepository {

    private val prefs = FlowPreferences(
        moshi = moshi,
        sharedPreferences = sharedPreferences,
        version = BuildConfig.PREFERENCES_VERSION,
        fallbackToDestructiveMigration = true
    )

    override var userEmailAddress: String? by prefs[ARG_USER_EMAIL, String::class]
}
