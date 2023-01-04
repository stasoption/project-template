package com.averin.android.developer.baseui.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.view.textOrHide
import com.averin.android.developer.baseui.widget.CustomButton
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.FrConfirmDialogBinding

class DoubleActionBottomSheet : BaseBottomSheetDialog() {

    private val binding by viewBinding(FrConfirmDialogBinding::bind)

    override val layoutRes: Int = R.layout.fr_confirm_dialog

    @StringRes
    private var cancelButtonText: Int = R.string.btn_cancel
    @StringRes
    private var confirmButtonText: Int = R.string.btn_ok

    private var cancelButtonType: CustomButton.Type = CustomButton.Type.SECONDARY
    private var confirmButtonType: CustomButton.Type = CustomButton.Type.PRIMARY

    private var cancelClickListener: (() -> Unit)? = null
    private var confirmClickListener: (() -> Unit)? = null

    private var isWithCloseButton: Boolean = true

    fun bindFirstActionButton(
        @StringRes text: Int = R.string.btn_cancel,
        type: CustomButton.Type = CustomButton.Type.SECONDARY,
        clickListener: (() -> Unit)? = null
    ) {
        cancelButtonText = text
        cancelButtonType = type
        cancelClickListener = clickListener
    }

    fun bindSecondActionButton(
        @StringRes text: Int = R.string.btn_ok,
        type: CustomButton.Type = CustomButton.Type.SECONDARY,
        clickListener: (() -> Unit)? = null
    ) {
        confirmButtonText = text
        confirmButtonType = type
        confirmClickListener = clickListener
    }

    fun isWithCloseButton(isWithButton: Boolean) {
        isWithCloseButton = isWithButton
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnCancel.buttonType = cancelButtonType
            btnConfirm.buttonType = confirmButtonType

            btnCancel.buttonText = getString(cancelButtonText)
            btnConfirm.buttonText = getString(confirmButtonText)

            tvTitle.textOrHide(arguments?.getString(ARG_TITLE))
            tvMessage.textOrHide(arguments?.getString(ARG_MESSAGE))
            ivClose.isVisible = isWithCloseButton
            ivClose.setOnClickListener {
                dismissAllowingStateLoss()
            }
            btnCancel.onClickListener = {
                cancelClickListener?.invoke()
                dismissAllowingStateLoss()
            }
            btnConfirm.onClickListener = {
                confirmClickListener?.invoke()
                dismissAllowingStateLoss()
            }
        }
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        fun newInstance(message: String, title: String? = null) = DoubleActionBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_MESSAGE, message)
                putString(ARG_TITLE, title)
            }
        }
    }
}
