package com.averin.android.developer.customview.presentation.tabs

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.showToast
import com.averin.android.developer.baseui.extension.androidx.fragment.app.showMessageDialog
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.baseui.widget.CustomButton
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrTab2Binding

class Tab2Fragment : BaseFragment(R.layout.fr_tab_2) {
    private val binding by viewBinding(FrTab2Binding::bind)
    override val viewModel: BaseViewModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            /*
           * Custom Button
           * */
            primaryEnabled.apply {
                buttonText = "Primary Enabled"
                buttonType = CustomButton.Type.PRIMARY
                buttonIcon = R.drawable.ic_trash
                onClickListener = {
                    context?.showToast("Click")
                }
            }

            /*
            * Custom RadioButton
            * */
            rbRadioButton1.onSuggestionClickListener = {
                showMessageDialog(getString(R.string.template_middle_text))
            }
        }
    }

    companion object {
        fun newInstance() = Tab2Fragment()
    }
}
