package com.averin.android.developer.baseui.extension.android.content.res

import android.content.res.Resources
import android.util.TypedValue

private const val DP_DENSITY = 160f

fun Resources.pxToDp(px: Float): Float = px / (displayMetrics.densityDpi.toFloat() / DP_DENSITY)

fun Resources.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics).toInt()
}

fun Resources.spToPx(sp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics).toInt()
}
