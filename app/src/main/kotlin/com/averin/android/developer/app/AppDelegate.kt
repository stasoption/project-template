package com.averin.android.developer.app

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.averin.android.developer.app.init.initAppAttachBaseContext
import com.averin.android.developer.app.init.initAppCreate
import timber.log.Timber

class AppDelegate : MultiDexApplication() {

    override fun onCreate() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initAppCreate(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // Code called before ContentProvider.onCreate method
        initAppAttachBaseContext(this)
    }
}
