package com.averin.android.developer.baseui.extension.android.view

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.TextWatcher
import android.widget.TextView
import androidx.core.view.isVisible

inline fun TextView.addTextChangedListener(init: KTextWatcher.() -> Unit): TextWatcher {
    return KTextWatcher().apply {
        init()
        addTextChangedListener(this)
    }
}
fun TextView.textOrHide(value: String?) {
    text = value
    isVisible = !value.isNullOrEmpty()
}

fun TextView.setIsEnabled(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.alpha = if (isEnabled) {
        1F
    } else {
        0.4F
    }
}

fun TextView.setCompoundDrawable(
    leftIconId: Int = 0,
    topIconId: Int = 0,
    endIconId: Int = 0,
    bottomIconId: Int = 0
) {
    this.setCompoundDrawablesWithIntrinsicBounds(leftIconId, topIconId, endIconId, bottomIconId)
}

fun TextView.setDrawableTintColor(color: Int) {
    this.compoundDrawables.forEach { drawable ->
        drawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}
