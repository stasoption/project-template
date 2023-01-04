package com.averin.android.developer.app.navigation

import androidx.fragment.app.FragmentManager
import com.averin.android.developer.app.main.navigation.MainNavControllerStorage
import com.averin.android.developer.app.navigation.bottom.presentation.BottomNavViewModel
import com.averin.android.developer.app.navigation.bottom.presentation.BottomNavigation
import com.averin.android.developer.app.navigation.delegates.AuthNavigationDelegate
import com.averin.android.developer.app.navigation.delegates.BottomNavigationDelegate
import com.averin.android.developer.app.navigation.delegates.CustomViewsNavigationDelegate
import com.averin.android.developer.app.navigation.delegates.BottomTab4NavigationDelegate
import com.averin.android.developer.app.navigation.delegates.ComposeNavigationDelegate
import com.averin.android.developer.app.navigation.delegates.SettingsNavigationDelegate
import com.averin.android.developer.app.navigation.delegates.GitHubNavigationDelegate
import com.averin.android.developer.auth.navigation.AuthNavControllerStorage
import com.averin.android.developer.auth.navigation.AuthNavigation
import com.averin.android.developer.compose.navigation.ComposeNavControllerStorage
import com.averin.android.developer.compose.navigation.ComposeNavigation
import com.averin.android.developer.bottomtab4.navigation.BottomTab4NavControllerStorage
import com.averin.android.developer.bottomtab4.navigation.BottomTab4Navigation
import com.averin.android.developer.customview.navigation.CustomViewsNavControllerStorage
import com.averin.android.developer.customview.navigation.CustomViewsNavigation
import com.averin.android.developer.settings.navigation.SettingsNavControllerStorage
import com.averin.android.developer.settings.navigation.SettingsNavigation
import com.averin.android.developer.github.navigation.GitHubNavControllerStorage
import com.averin.android.developer.github.navigation.GitHubNavigation
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {
    single { MainNavControllerStorage() }
    single { AuthNavControllerStorage() }

    single<AuthNavigation> { AuthNavigationDelegate(get()) }

    /* Tabs start */
    single { GitHubNavControllerStorage() }
    single<GitHubNavigation> { GitHubNavigationDelegate(get()) }

    single { CustomViewsNavControllerStorage() }
    single<CustomViewsNavigation> { CustomViewsNavigationDelegate(get()) }

    single { ComposeNavControllerStorage() }
    single<ComposeNavigation> { ComposeNavigationDelegate(get()) }

    single { BottomTab4NavControllerStorage() }
    single<BottomTab4Navigation> { BottomTab4NavigationDelegate(get()) }

    single { SettingsNavControllerStorage() }
    single<SettingsNavigation> { SettingsNavigationDelegate(get(), get()) }
    /* Tabs end */

    // DI survives application close and we need to use another fragment manager every time for bottom navigation
    factory<BottomNavigation> { (fm: FragmentManager) ->
        BottomNavigationDelegate(get(), fm)
    }

    // BottomNav
    viewModel { BottomNavViewModel() }
}
