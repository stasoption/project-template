package com.averin.android.developer.app.navigation.delegates

import com.averin.android.developer.MainGraphDirections
import com.averin.android.developer.app.main.navigation.MainNavControllerStorage
import com.averin.android.developer.auth.navigation.AuthNavigation

class AuthNavigationDelegate(
    private val mainNavControllerStorage: MainNavControllerStorage
) : AuthNavigation {
    override fun openDashboard() {
        mainNavControllerStorage.navController
            ?.navigate(MainGraphDirections.rootPopupAllOpenMain())
    }
}
