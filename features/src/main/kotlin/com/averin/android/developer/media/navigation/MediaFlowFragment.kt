package com.averin.android.developer.media.navigation

import com.averin.android.developer.baseui.presentation.fragment.BaseFlowFragment
import com.averin.android.developer.dashboard.R
import org.koin.android.ext.android.inject

class MediaFlowFragment : BaseFlowFragment() {
    override val navControllerStorage: MediaNavControllerStorage by inject()
    override val graphResId: Int = R.navigation.media_graph
}
