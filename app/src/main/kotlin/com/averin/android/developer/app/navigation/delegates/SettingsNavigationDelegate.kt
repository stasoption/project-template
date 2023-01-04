package com.averin.android.developer.app.navigation.delegates

import com.averin.android.developer.MainGraphDirections
import com.averin.android.developer.app.main.navigation.MainNavControllerStorage
import com.averin.android.developer.settings.navigation.SettingsNavControllerStorage
import com.averin.android.developer.settings.navigation.SettingsNavigation

class SettingsNavigationDelegate(
    private val settingsNavControllerStorage: SettingsNavControllerStorage,
    private val mainNavControllerStorage: MainNavControllerStorage,
) : SettingsNavigation {

    override fun openLogin() {
        mainNavControllerStorage.navController
            ?.navigate(MainGraphDirections.rootPopupAllOpenAuth())
    }
}
