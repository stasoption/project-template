package com.averin.android.developer.bottomtab4.navigation

import com.averin.android.developer.baseui.presentation.fragment.BaseFlowFragment
import com.averin.android.developer.dashboard.R
import org.koin.android.ext.android.inject

class BottomTab4FlowFragment : BaseFlowFragment() {
    override val navControllerStorage: BottomTab4NavControllerStorage by inject()
    override val graphResId: Int = R.navigation.bottom_tab_4_graph
}
