package com.averin.android.developer.app.init

import android.app.Application
import com.averin.android.developer.base.init.BaseAppInitializer
import com.averin.android.developer.baseui.VisibilityLifecycleCallback

class AppVisibilityInitializer(private val application: Application) : BaseAppInitializer {
    override fun onAppCreateInit() {
        VisibilityLifecycleCallback.register(application)
    }
}
