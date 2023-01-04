package com.averin.android.developer.github.navigation

import com.averin.android.developer.baseui.presentation.fragment.BaseFlowFragment
import com.averin.android.developer.dashboard.R
import org.koin.android.ext.android.inject

class GitHubFlowFragment : BaseFlowFragment() {
    override val navControllerStorage: GitHubNavControllerStorage by inject()
    override val graphResId: Int = R.navigation.projects_graph
}
