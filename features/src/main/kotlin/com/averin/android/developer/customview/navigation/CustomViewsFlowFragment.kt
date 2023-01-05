package com.averin.android.developer.customview.navigation

import com.averin.android.developer.baseui.presentation.fragment.BaseFlowFragment
import com.averin.android.developer.dashboard.R
import org.koin.android.ext.android.inject

class CustomViewsFlowFragment : BaseFlowFragment() {
    override val navControllerStorage: CustomViewsNavControllerStorage by inject()
    override val graphResId: Int = R.navigation.custom_views_graph
}
