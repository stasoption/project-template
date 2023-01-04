package com.averin.android.developer.base.extension.kotlin

import android.content.res.Resources
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.roundToInt

private const val FORMAT_FLOAT = "###,##0.00"
private const val FORMAT_INT = "###,###.#"

fun Float?.toAmountFormat(withFraction: Boolean = false): String {
    this ?: return ""
    val format = if (withFraction) {
        FORMAT_FLOAT
    } else {
        FORMAT_INT
    }
    val formatter: NumberFormat = DecimalFormat(format)
    return formatter.format(this)
}

fun Int?.toAmountFormat(): String {
    this ?: return ""
    val formatter: NumberFormat = DecimalFormat(FORMAT_INT)
    return formatter.format(this)
}

val Int.toDP: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
