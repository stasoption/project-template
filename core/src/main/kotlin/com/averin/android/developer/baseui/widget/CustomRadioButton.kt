package com.averin.android.developer.baseui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatRadioButton

class CustomRadioButton(context: Context, attributeSet: AttributeSet) : AppCompatRadioButton(context, attributeSet) {

    var onSuggestionClickListener: (() -> Unit)? = null

    override fun setEnabled(isEnabled: Boolean) {
        this.alpha = if (isEnabled) {
            1F
        } else {
            0.4F
        }
        super.setEnabled(isEnabled)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val compoundDrawableRight = compoundDrawables[RIGHT_DRAWABLE_INDEX]
        return if (compoundDrawableRight == null) {
            super.onTouchEvent(event)
        } else {
            val bounds = compoundDrawableRight.bounds
            val eventX = width - compoundDrawableRight.intrinsicWidth - event.x.toInt()
            val eventY = event.y.toInt()

            if (bounds.contains(eventX, eventY)) {
                onSuggestionClickListener?.invoke()
                false
            } else {
                super.onTouchEvent(event)
            }
        }
    }

    companion object {
        private const val RIGHT_DRAWABLE_INDEX = 2
    }
}
