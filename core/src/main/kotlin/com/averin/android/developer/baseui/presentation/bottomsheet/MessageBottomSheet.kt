package com.averin.android.developer.baseui.presentation.bottomsheet

import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.view.textOrHide
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.FrMessageBinding

class MessageBottomSheet : BaseBottomSheetDialog() {

    private val binding by viewBinding(FrMessageBinding::bind)
    override val layoutRes: Int = R.layout.fr_message

    private val iconId: Int by lazy { arguments?.getInt(ARG_ICON) ?: R.drawable.ic_ok }
    private val title: String? by lazy { arguments?.getString(ARG_TITLE) }
    private val message: String? by lazy { arguments?.getString(ARG_MESSAGE) }

    private var btnActonText: Int? = null
    private var btnActonListener: (() -> Unit)? = null

    var onCloseDialogListener: (() -> Unit)? = null

    fun bindAction(textId: Int, action: (() -> Unit)) {
        btnActonText = textId
        btnActonListener = action
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            ivIcon.setImageResource(iconId)
            tvTitle.textOrHide(title)

            tvMessage.text = SpannableString(Html.fromHtml(message))
            tvMessage.isVisible = !message.isNullOrEmpty()

            btnActonText?.let { binding.btnActon.buttonText = getString(it) }
            btnActonListener?.let {
                binding.btnActon.onClickListener = {
                    it.invoke()
                    dismissAllowingStateLoss()
                }
            }
            binding.btnActon.isVisible = btnActonText != null && btnActonListener != null
        }
    }

    override fun onDialogHidden() {
        onCloseDialogListener?.invoke()
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"
        private const val ARG_ICON = "iconId"
        fun newInstance(
            message: String,
            @DrawableRes iconId: Int = R.drawable.ic_ok,
            title: String? = null
        ) = MessageBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putInt(ARG_ICON, iconId)
            }
        }
    }
}
