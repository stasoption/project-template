package com.averin.android.developer.customview.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.showToast
import com.averin.android.developer.baseui.extension.androidx.fragment.app.hideKeyBoard
import com.averin.android.developer.baseui.extension.androidx.fragment.app.onBackPressed
import com.averin.android.developer.baseui.extension.androidx.fragment.app.showDialogWithAction
import com.averin.android.developer.baseui.extension.androidx.fragment.app.showMessageDialog
import com.averin.android.developer.baseui.extension.androidx.fragment.app.showWarningDialog
import com.averin.android.developer.baseui.extension.androidx.fragment.app.supportFragmentManager
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.bottomsheet.DoubleActionBottomSheet
import com.averin.android.developer.baseui.presentation.bottomsheet.DynamicMenuBottomSheet
import com.averin.android.developer.baseui.presentation.bottomsheet.MessageOnFullScreenBottomSheet
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.baseui.widget.CustomButton
import com.averin.android.developer.baseui.widget.CustomSegmentedView
import com.averin.android.developer.customview.presentation.tabs.Tab3Fragment
import com.averin.android.developer.customview.presentation.tabs.Tab2Fragment
import com.averin.android.developer.customview.presentation.tabs.Tab1Fragment
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrCustomViewsBinding

class CustomViewsFragment : BaseFragment(R.layout.fr_custom_views) {

    override val viewModel: BaseViewModel? = null

    private val binding by viewBinding(FrCustomViewsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { onBackPressed() }
        binding.run {
            toolbar.apply {
                onBackClickListener = {
                    onBackPressed()
                }
                linkText = "Menu"
                onLinkClickListener = { showDynamicMenuDialog() }
            }
            toggleView.apply {
                onClickListener = { button ->
                    hideKeyBoard()
                    when (button) {
                        CustomSegmentedView.Button.LEFT -> { viewPager.currentItem = PAGE_FIELDS }
                        CustomSegmentedView.Button.CENTRAL -> { viewPager.currentItem = PAGE_BUTTONS }
                        CustomSegmentedView.Button.RIGHT -> { viewPager.currentItem = PAGE_WIDGETS }
                        else -> { }
                    }
                }
            }
            viewPager.adapter = PagerFragmentAdapter(childFragmentManager)
            viewPager.offscreenPageLimit = PAGE_NUMBER
        }
    }

    override fun onResume() {
        super.onResume()
        binding.toggleView.currentTubWithoutNotify = when (binding.viewPager.currentItem) {
            PAGE_FIELDS -> CustomSegmentedView.Button.LEFT
            PAGE_BUTTONS -> CustomSegmentedView.Button.CENTRAL
            PAGE_WIDGETS -> CustomSegmentedView.Button.RIGHT
            else -> throw IllegalStateException("position=${binding.viewPager.currentItem}")
        }
    }

    private fun showDynamicMenuDialog() {
        DynamicMenuBottomSheet.newInstance("Dynamic Menu" /* it is not necessary argument */).apply {
            /* menu items. might be with icon or without */
            items = listOf(
                DynamicMenuBottomSheet.MenuItem(
                    R.string.example_menu_double_action,
                    R.drawable.ic_debug_placeholder
                ) {
                    showDoubleActionDialog()
                },
                DynamicMenuBottomSheet.MenuItem(
                    R.string.example_menu_single_action,
                    R.drawable.ic_debug_placeholder
                ) {
                    showSingleActionDialog()
                },
                DynamicMenuBottomSheet.MenuItem(
                    R.string.example_menu_simple_dialog,
                    R.drawable.ic_debug_placeholder
                ) {
                    showSimpleDialog()
                },
                DynamicMenuBottomSheet.MenuItem(
                    R.string.example_menu_warning_dialog,
                    R.drawable.ic_debug_placeholder
                ) {
                    showWarningDialog()
                },
                DynamicMenuBottomSheet.MenuItem(
                    R.string.example_menu_full_screen_message,
                    R.drawable.ic_debug_placeholder
                ) {
                    showMessageOnFullScreenDialog()
                }
            )
        }.show(supportFragmentManager(), DynamicMenuBottomSheet::class.java.name)
    }

    private fun showDoubleActionDialog() {
        DoubleActionBottomSheet.newInstance("Double Action Dialog").apply {
            bindFirstActionButton(
                text = R.string.no_cancel,
                type = CustomButton.Type.SECONDARY
            )
            bindSecondActionButton(
                text = R.string.yes_delete,
                type = CustomButton.Type.WARNING
            ) {
                context?.showToast("Deleted")
            }
        }.show(supportFragmentManager(), DoubleActionBottomSheet::class.java.name)
    }

    private fun showSingleActionDialog() {
        showDialogWithAction(
            message = getString(R.string.template_middle_text),
            iconId = R.drawable.ic_warning_big,
            buttonTextId = R.string.btn_ok,
            clickActionListener = {
                context?.showToast("Ok")
            }
        )
    }

    private fun showSimpleDialog() {
        showMessageDialog(getString(R.string.template_middle_text))
    }

    private fun showWarningDialog() {
        showWarningDialog(getString(R.string.template_middle_text))
    }

    private fun showMessageOnFullScreenDialog() {
        MessageOnFullScreenBottomSheet.show(
            fragmentManager = supportFragmentManager(),
            image = R.drawable.ic_warning_filled,
            message = getString(R.string.template_long_text),
            /*
                You can show one or two button. When you add text for the button you will add the button
            */
            leftButtonText = getString(R.string.btn_cancel),
            rightButtonText = getString(R.string.btn_ok),
            onLeftButtonListener = {
                context?.showToast("Left Button Click")
            },
            onRightButtonListener = {
                context?.showToast("Right Button Click")
            }
        )
    }

    private inner class PagerFragmentAdapter(
        fragmentManager: FragmentManager
    ) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = PAGE_NUMBER
        override fun getItem(position: Int): Fragment = when (position) {
            PAGE_FIELDS -> Tab1Fragment.newInstance()
            PAGE_BUTTONS -> Tab2Fragment.newInstance()
            PAGE_WIDGETS -> Tab3Fragment.newInstance()
            else -> throw IllegalStateException("position=$position")
        }
    }

    companion object {
        private const val PAGE_FIELDS = 0
        private const val PAGE_BUTTONS = 1
        private const val PAGE_WIDGETS = 2
        private const val PAGE_NUMBER = 3
    }
}
