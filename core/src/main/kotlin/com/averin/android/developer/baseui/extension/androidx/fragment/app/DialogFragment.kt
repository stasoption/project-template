package com.averin.android.developer.baseui.extension.androidx.fragment.app

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment

fun DialogFragment.makeFullscreen() {
    dialog?.window?.run {
        setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
    }
}

fun DialogFragment.makeTransparent() {
    dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
}
