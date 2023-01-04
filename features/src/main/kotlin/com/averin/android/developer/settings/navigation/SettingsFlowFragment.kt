package com.averin.android.developer.settings.navigation

import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFlowFragment
import com.averin.android.developer.dashboard.R
import org.koin.android.ext.android.inject

class SettingsFlowFragment : BaseFlowFragment() {
    override val navControllerStorage: SettingsNavControllerStorage by inject()
    override val graphResId = R.navigation.settings_graph
    val viewModel: BaseViewModel? = null
}
