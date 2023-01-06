package com.averin.android.developer.app.init

import android.app.Application
import com.averin.android.developer.app.appModule
import com.averin.android.developer.app.main.mainModule
import com.averin.android.developer.app.navigation.navigationModule
import com.averin.android.developer.app.network.rest.restModule
import com.averin.android.developer.app.splash.splashModule
import com.averin.android.developer.auth.authModule
import com.averin.android.developer.base.baseModule
import com.averin.android.developer.base.init.BaseAppInitializer
import com.averin.android.developer.baseui.presentation.bottomsheet.webview.webViewModule
import com.averin.android.developer.customview.customViewsModule
import com.averin.android.developer.media.mediaModule
import com.averin.android.developer.compose.composeModule
import com.averin.android.developer.settings.settingsModule
import com.averin.android.developer.github.gitHubProjectsModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initAppCreate(application: Application) {
    application.getKoin().getAll<BaseAppInitializer>()
        .sortedBy { it.getPriority() }
        .forEach { it.onAppCreateInit() }
}

fun initAppAttachBaseContext(application: Application) {
    startKoin {
        androidContext(application)
        modules(
            listOf(
                appModule, webViewModule, restModule, navigationModule, baseModule, splashModule, mainModule, authModule,
                gitHubProjectsModule, customViewsModule, composeModule, mediaModule, settingsModule
            )
        )
    }
}
