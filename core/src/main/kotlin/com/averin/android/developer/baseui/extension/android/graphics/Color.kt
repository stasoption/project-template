package com.averin.android.developer.baseui.extension.android.graphics

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

private const val MAX_OPACITY = 255f

fun colorWithOpacity(@FloatRange(from = 0.0, to = 1.0) opacity: Float, @ColorInt color: Int): Int {
    return Color.argb((MAX_OPACITY * opacity).toInt(), Color.red(color), Color.green(color), Color.blue(color))
}
