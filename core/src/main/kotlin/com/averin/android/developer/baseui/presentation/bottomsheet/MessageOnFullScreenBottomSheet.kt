package com.averin.android.developer.baseui.presentation.bottomsheet

import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.view.textOrHide
import com.averin.android.developer.baseui.widget.CustomButton
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.FrMessageOnFullScreenBinding
import timber.log.Timber

open class MessageOnFullScreenBottomSheet : BaseFullScreenBottomSheetFragment(R.layout.fr_message_on_full_screen) {

    override val canBeHidden: Boolean = false
    override val canDraggable: Boolean = false
    override val backgroundDrawable: Int = R.drawable.bg_full_bottomsheet

    private val binding by viewBinding(FrMessageOnFullScreenBinding::bind)

    private val image: Int? by lazy { arguments?.getInt(ARG_IMAGE) }
    private val title: String? by lazy { arguments?.getString(ARG_TITLE) }
    private val message: String? by lazy { arguments?.getString(ARG_MESSAGE) }
    private val leftButtonText: String? by lazy { arguments?.getString(ARG_LEFT_BTN_TEXT) }
    private val rightButtonText: String? by lazy { arguments?.getString(ARG_RIGHT_BTN_TEXT) }

    var leftButtonType: CustomButton.Type = CustomButton.Type.SECONDARY
    var rightButtonType: CustomButton.Type = CustomButton.Type.PRIMARY

    var onLeftButtonClickListener: (() -> Unit)? = null
    var onRightButtonClickListener: (() -> Unit)? = null

    override fun onResume() {
        super.onResume()
        /*
        * invoke main action (right button by default), when system back button has been pressed
        * */
        dialog?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                onRightButtonClickListener?.invoke()
            }
            return@setOnKeyListener false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            ivImage.setImageResource(image ?: R.drawable.ic_warning)
            tvTitle.textOrHide(title)
            val parsedMessage = if (message != null) {
                SpannableString(Html.fromHtml(message))
            } else {
                null
            }
            tvMessage.text = parsedMessage
            tvMessage.isVisible = !parsedMessage.isNullOrEmpty()

            btnLeft.isVisible = leftButtonText != null
            btnLeft.buttonText = leftButtonText ?: ""
            btnLeft.buttonType = leftButtonType
            btnLeft.onClickListener = {
                onLeftButtonClickListener?.invoke()
                dismissAllowingStateLoss()
            }

            btnRight.isVisible = rightButtonText != null
            btnRight.buttonText = rightButtonText ?: ""
            btnRight.buttonType = rightButtonType
            btnRight.onClickListener = {
                onRightButtonClickListener?.invoke()
                dismissAllowingStateLoss()
            }
        }
    }

    companion object {
        private const val ARG_IMAGE = "image"
        const val ARG_TITLE = "title"
        const val ARG_MESSAGE = "message"
        const val ARG_LEFT_BTN_TEXT = "leftButtonText"
        const val ARG_RIGHT_BTN_TEXT = "rightButtonText"

        fun newInstance(
            message: String,
            title: String? = null,
            image: Int = R.drawable.ic_warning,
            leftButtonText: String? = null,
            rightButtonText: String? = null,
        ) = MessageOnFullScreenBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_MESSAGE, message)
                putString(ARG_TITLE, title)
                putInt(ARG_IMAGE, image)
                putString(ARG_LEFT_BTN_TEXT, leftButtonText)
                putString(ARG_RIGHT_BTN_TEXT, rightButtonText)
            }
        }

        fun show(
            fragmentManager: FragmentManager,
            message: String,
            title: String? = null,
            image: Int = R.drawable.ic_warning,
            leftButtonText: String? = null,
            rightButtonText: String? = null,
            leftButtonTypeId: CustomButton.Type = CustomButton.Type.SECONDARY,
            rightButtonTypeId: CustomButton.Type = CustomButton.Type.PRIMARY,
            onLeftButtonListener: (() -> Unit)? = null,
            onRightButtonListener: (() -> Unit)? = null,
        ) {
            val tag = MessageOnFullScreenBottomSheet::class.java.name
            if (fragmentManager.findFragmentByTag(tag) != null) {
                Timber.e("Fragment with tag=$tag has been already created")
                return
            }
            newInstance(
                message = message,
                title = title,
                image = image,
                leftButtonText = leftButtonText,
                rightButtonText = rightButtonText

            ).apply {
                leftButtonType = leftButtonTypeId
                rightButtonType = rightButtonTypeId
                onLeftButtonClickListener = onLeftButtonListener
                onRightButtonClickListener = onRightButtonListener
            }.show(fragmentManager, tag)
        }
    }
}
