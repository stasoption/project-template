package com.averin.android.developer.app.main.navigation

import com.averin.android.developer.R
import com.averin.android.developer.baseui.presentation.fragment.BaseFlowFragment
import com.averin.android.developer.baseui.util.delegates.RequiredArgument
import org.koin.android.ext.android.inject

class MainFlowFragment : BaseFlowFragment() {

    override val navControllerStorage: MainNavControllerStorage by inject()
    override val graphResId = R.navigation.main_graph
    private var authorized: Boolean by RequiredArgument

    override fun onBackPressed() {
        if (navController.previousBackStackEntry != null && navController.popBackStack()) {
            return
        }
        activity?.supportFinishAfterTransition()
    }

    override fun setupNavGraph() {
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(graphResId)
        graph.startDestination = if (authorized) R.id.bottom_nav else R.id.auth_flow
        val startDestArgs = onCreateGraph(graph)
        navController.setGraph(graph, startDestArgs)
    }

    companion object {
        fun newInstance(isAuthorized: Boolean) = MainFlowFragment().apply {
            authorized = isAuthorized
        }
    }
}
