package com.averin.android.developer.app.navigation.bottom.presentation

import com.averin.android.developer.app.main.navigation.BottomTab

interface BottomNavigation {
    fun switchTab(tab: BottomTab)
    fun openLogin()
}
