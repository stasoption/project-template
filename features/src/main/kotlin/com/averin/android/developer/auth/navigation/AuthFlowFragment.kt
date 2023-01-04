package com.averin.android.developer.auth.navigation

import com.averin.android.developer.baseui.presentation.fragment.BaseFlowFragment
import com.averin.android.developer.dashboard.R
import org.koin.android.ext.android.inject

class AuthFlowFragment : BaseFlowFragment() {
    override val navControllerStorage: AuthNavControllerStorage by inject()
    override val graphResId = R.navigation.auth_graph
}
