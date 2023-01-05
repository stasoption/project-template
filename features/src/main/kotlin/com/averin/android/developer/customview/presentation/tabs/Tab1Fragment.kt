package com.averin.android.developer.customview.presentation.tabs

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.base.util.toUIFormat
import com.averin.android.developer.baseui.extension.android.content.showToast
import com.averin.android.developer.baseui.extension.android.view.showDatePicker
import com.averin.android.developer.baseui.extension.androidx.fragment.app.supportFragmentManager
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrTab1Binding
import timber.log.Timber

class Tab1Fragment : BaseFragment(R.layout.fr_tab_1) {

    override val viewModel: BaseViewModel? = null
    private val binding by viewBinding(FrTab1Binding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            /*
           * ExpandedTextView
           * */
            expandedTextView.text = getString(R.string.template_long_text)
            /*
            * InputView
            * */
            inputView.init {
                Timber.d("InputView=$it")
            }
            inputView.bindLabelSuggestion {
                context?.showToast("Field suggestion")
            }
            inputView.bindSymbolCounter(15)

            /*
            * SelectView
            * */
            selectView.onSelectViewListener = {
                chooseBirthday()
            }

            /*
            * MoneyView
            * */
            moneyView.init { source, formatted ->
                Timber.d("sourceAmount=$source, formattedAmount=$formatted")
            }

            /*
            * PasswordInputView
            * */
            inputView.init {
                Timber.d("password=$it")
            }

            /*
            * InputBox
            * */
            inputBox.init {
                Timber.d("InputBox=$it")
            }
            inputBox.showError("Example of error")

            /*
            * SearchingView
            * */
            searchingView.apply {
                onQueryChangeListener = { query -> Timber.e(query) }
                onClearClickListener = { }
            }
        }
    }

    private fun chooseBirthday() {
        binding.selectView.showDatePicker(
            fragmentManager = supportFragmentManager(),
            onDateSelected = {
                binding.selectView.textValue = it.toUIFormat()
            }
        )
    }

    companion object {
        fun newInstance() = Tab1Fragment()
    }
}
