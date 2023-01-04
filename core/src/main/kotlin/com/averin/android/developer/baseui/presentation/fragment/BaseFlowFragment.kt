package com.averin.android.developer.baseui.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavGraph
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.averin.android.developer.baseui.extension.androidx.fragment.app.onBackPressed
import com.averin.android.developer.baseui.extension.androidx.fragment.app.parentFragmentOnBackPressed
import com.averin.android.developer.baseui.navigation.FragmentAddNavigator
import com.averin.android.developer.baseui.navigation.NavControllerStorage
import com.averin.android.developer.baseui.presentation.BackPressable
import com.averin.android.developer.core.R

abstract class BaseFlowFragment : NavHostFragment(), BackPressable {
    abstract val navControllerStorage: NavControllerStorage?
    abstract val graphResId: Int

    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return FragmentAddNavigator(
            requireContext(),
            childFragmentManager,
            getContainerId()
        )
    }

    /**
     * A copy of [NavHostFragment.getContainerId] private function
     */
    private fun getContainerId(): Int {
        val id = id
        return if (id != 0 && id != View.NO_ID) {
            id
        } else {
            R.id.nav_host_fragment_container
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavGraph()
    }

    override fun onResume() {
        super.onResume()
        navControllerStorage?.navController = navController
    }

    override fun onPause() {
        super.onPause()
        navControllerStorage?.navController = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { onBackPressed() }
    }

    override fun onBackPressed() {
        if (navController.previousBackStackEntry != null && navController.popBackStack()) {
            return
        }
        parentFragmentOnBackPressed()
    }

    open fun setupNavGraph() {
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(graphResId)
        val startDestArgs = onCreateGraph(graph)
        navController.setGraph(graph, startDestArgs)
    }

    open fun onCreateGraph(graph: NavGraph): Bundle? = null
}
