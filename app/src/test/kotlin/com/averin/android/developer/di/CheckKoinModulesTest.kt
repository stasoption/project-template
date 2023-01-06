package com.averin.android.developer.di

import android.app.Application
import android.content.SharedPreferences
import com.averin.android.developer.app.appModule
import com.averin.android.developer.app.main.mainModule
import com.averin.android.developer.app.navigation.navigationModule
import com.averin.android.developer.app.network.rest.restModule
import com.averin.android.developer.app.splash.splashModule
import com.averin.android.developer.auth.authModule
import com.averin.android.developer.base.baseModule
import com.averin.android.developer.baseui.presentation.bottomsheet.webview.webViewModule
import com.averin.android.developer.compose.composeModule
import com.averin.android.developer.customview.customViewsModule
import com.averin.android.developer.github.gitHubProjectsModule
import com.averin.android.developer.media.mediaModule
import com.averin.android.developer.settings.settingsModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class CheckKoinModulesTest : KoinTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz -> mock(clazz.java) }

    val mockedAndroidContext = mock(Application::class.java)

    @Test
    fun testRemoteConfiguration() {
        val koinApp = koinApplication {
            androidContext(mockedAndroidContext)
            modules(
                listOf(
                    appModule, webViewModule, restModule, navigationModule, baseModule, splashModule, mainModule, authModule,
                    gitHubProjectsModule, customViewsModule, composeModule, mediaModule, settingsModule
                )
            )
        }
        koinApp.koin.declareMock<SharedPreferences>()
        koinApp.checkModules()
    }
}
