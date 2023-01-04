package com.averin.android.developer.app.navigation.bottom.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.R
import com.averin.android.developer.baseui.extension.androidx.fragment.app.hideKeyBoard
import com.averin.android.developer.baseui.extension.androidx.fragment.app.parentFragmentOnBackPressed
import com.averin.android.developer.baseui.extension.androidx.fragment.app.post
import com.averin.android.developer.baseui.extension.androidx.fragment.app.setKeyboardEventListener
import com.averin.android.developer.baseui.extension.androidx.lifecycle.observeSafe
import com.averin.android.developer.baseui.presentation.NavigationPanel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.databinding.FrBottomNavBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BottomNavFragment : BaseFragment(R.layout.fr_bottom_nav), NavigationPanel {

    override val viewModel: BottomNavViewModel by viewModel()

    private val binding by viewBinding(FrBottomNavBinding::bind)
    private val navigation: BottomNavigation by inject { parametersOf(childFragmentManager) }

    private var isNavigationPanelVisible: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            bottomNavigationView.apply { onTabClickListener = { viewModel.selectTab(it) } }
        }
        viewModel.selectedTab.observeSafe(viewLifecycleOwner) {
            post {
                hideKeyBoard()
                navigation.switchTab(it)
            }
        }
        viewModel.updateNotificationEvent.observeSafe(viewLifecycleOwner) {
            binding.bottomNavigationView.notificationCount = it
        }
        setKeyboardEventListener { isOpen ->
            binding.bottomNavigationView.isVisible = !isOpen && isNavigationPanelVisible
        }
    }

    private fun selectTabWithoutNotify(menuId: Int) {
        binding.bottomNavigationView.setCurrentTab(
            id = menuId,
            isWithoutNotify = false
        )
    }

    override fun onBackPressed() {
        val previousTab = viewModel.openPreviousTab()
        if (previousTab != null) {
            selectTabWithoutNotify(previousTab.menuId)
            return
        }
        parentFragmentOnBackPressed()
    }

    override fun isNavigationPanelVisible(isVisibility: Boolean) {
        isNavigationPanelVisible = isVisibility
        binding.bottomNavigationView.isVisible = isNavigationPanelVisible
    }
}
