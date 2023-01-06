package com.averin.android.developer.app.main.navigation

import androidx.fragment.app.Fragment
import com.averin.android.developer.app.navigation.bottom.presentation.CustomBottomNavigationView
import com.averin.android.developer.media.navigation.MediaFlowFragment
import com.averin.android.developer.compose.navigation.ComposeFlowFragment
import com.averin.android.developer.customview.navigation.CustomViewsFlowFragment
import com.averin.android.developer.github.navigation.GitHubFlowFragment
import com.averin.android.developer.settings.navigation.SettingsFlowFragment

sealed class BottomTab(
    val tag: String,
    val menuId: Int
) {
    abstract fun createFragment(): Fragment
}

object BottomTab1 : BottomTab(
    GitHubFlowFragment::class.java.name,
    CustomBottomNavigationView.TAB_1
) {
    override fun createFragment() = GitHubFlowFragment()
}

object BottomTab2 : BottomTab(
    CustomViewsFlowFragment::class.java.name,
    CustomBottomNavigationView.TAB_2
) {
    override fun createFragment() = CustomViewsFlowFragment()
}

object BottomTab3 : BottomTab(
    ComposeFlowFragment::class.java.name,
    CustomBottomNavigationView.TAB_3
) {
    override fun createFragment() = ComposeFlowFragment()
}

object BottomTab4 : BottomTab(
    MediaFlowFragment::class.java.name,
    CustomBottomNavigationView.TAB_4
) {
    override fun createFragment() = MediaFlowFragment()
}

object BottomTab5 : BottomTab(
    SettingsFlowFragment::class.java.name,
    CustomBottomNavigationView.TAB_5
) {
    override fun createFragment() = SettingsFlowFragment()
}
