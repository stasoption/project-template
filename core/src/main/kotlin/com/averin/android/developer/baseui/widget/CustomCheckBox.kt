package com.averin.android.developer.baseui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox

class CustomCheckBox(context: Context, attributeSet: AttributeSet) : AppCompatCheckBox(context, attributeSet) {
    override fun setEnabled(isEnabled: Boolean) {
        this.alpha = if (isEnabled) {
            1F
        } else {
            0.4F
        }
        super.setEnabled(isEnabled)
    }
}
