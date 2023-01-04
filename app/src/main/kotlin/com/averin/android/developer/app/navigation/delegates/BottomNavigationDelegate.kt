package com.averin.android.developer.app.navigation.delegates

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import com.averin.android.developer.MainGraphDirections
import com.averin.android.developer.R
import com.averin.android.developer.app.main.navigation.BottomTab
import com.averin.android.developer.app.main.navigation.MainNavControllerStorage
import com.averin.android.developer.app.navigation.bottom.presentation.BottomNavigation

class BottomNavigationDelegate(
    private val mainNavControllerStorage: MainNavControllerStorage,
    private val fragmentManager: FragmentManager
) : BottomNavigation {

    override fun switchTab(tab: BottomTab) {
        val currentFragment = fragmentManager.fragments.firstOrNull { !it.isHidden }
        val newFragment = fragmentManager.findFragmentByTag(tab.tag)

        if (isSameFragment(currentFragment, newFragment)) return

        fragmentManager.commitNow {
            if (newFragment == null) {
                add(R.id.bottomNavContainer, tab.createFragment(), tab.tag)
            }
            currentFragment?.let {
                hide(it)
                setMaxLifecycle(it, Lifecycle.State.CREATED)
            }
            newFragment?.let {
                show(it)
                setMaxLifecycle(it, Lifecycle.State.RESUMED)
            }
        }
    }

    override fun openLogin() {
        mainNavControllerStorage.navController
            ?.navigate(MainGraphDirections.rootPopupAllOpenAuth())
    }

    private fun isSameFragment(currentFragment: Fragment?, newFragment: Fragment?): Boolean {
        return currentFragment != null && newFragment != null && currentFragment == newFragment
    }
}
