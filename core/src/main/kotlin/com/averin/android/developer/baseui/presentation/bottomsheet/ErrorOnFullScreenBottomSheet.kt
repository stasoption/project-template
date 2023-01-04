package com.averin.android.developer.baseui.presentation.bottomsheet

import androidx.fragment.app.FragmentManager
import com.averin.android.developer.baseui.widget.CustomButton
import com.averin.android.developer.core.R
import timber.log.Timber

class ErrorOnFullScreenBottomSheet : MessageOnFullScreenBottomSheet() {

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            title: String,
            message: String,
            buttonText: String,
            onCloseErrorListener: (() -> Unit)? = null
        ) {
            val tag = ErrorOnFullScreenBottomSheet::class.java.name
            if (fragmentManager.findFragmentByTag(tag) != null) {
                Timber.e("Fragment $tag has been already created")
                return
            }
            newInstance(
                message = message,
                title = title,
                image = R.drawable.ic_error,
                rightButtonText = buttonText
            ).apply {
                rightButtonType = CustomButton.Type.WARNING
                onRightButtonClickListener = onCloseErrorListener
            }.show(fragmentManager, ErrorOnFullScreenBottomSheet::class.java.name)
        }
    }
}
