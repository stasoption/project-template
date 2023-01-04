package com.averin.android.developer.base

import androidx.preference.PreferenceManager
import com.averin.android.developer.base.util.FlowPreferences
import com.averin.android.developer.core.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val baseModule = module {
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    single {
        FlowPreferences(
            moshi = get(),
            sharedPreferences = get(),
            version = BuildConfig.PREFERENCES_VERSION,
            fallbackToDestructiveMigration = true
        )
    }
}
