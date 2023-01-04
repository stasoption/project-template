package com.averin.android.developer.base.extension.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.averin.android.developer.baseui.widget.imagepicker.ImagePreviewBottomSheet
import java.util.Locale
import java.util.UUID

const val NOT_DIGITS = "\\D+"
const val NOT_LETTERS = "\\w"
const val HTML_END_LINE = "<br />"
const val HTML_COLOR_PRIMARY_PREFIX = "<b><font color='#1DAA39'>"
const val HTML_COLOR_PRIMARY_POSTFIX = "</font></b>"
const val HTML_BOLD_PREFIX = "<b>"
const val HTML_BOLD_POSTFIX = "</b>"

fun String?.removeNoDigits(): String? = this?.let { Regex(NOT_DIGITS).replace(it, "") }

fun String?.removeLetters(): String? = this?.let { Regex(NOT_LETTERS).replace(it, "") }

fun String?.toNumber(): Int? = if (this.isNullOrEmpty()) {
    null
} else {
    this.removeNoDigits()?.toInt()
}

fun String.toSimpleFileName(): String {
    return this.replace("@", "_")
        .replace(".", "_")
        .replace(" ", "_")
}

fun String?.showFullScreenImageUrl(activity: AppCompatActivity) {
    if (this == null) return
    ImagePreviewBottomSheet.loadImageUrl(this)
        .show(activity.supportFragmentManager, ImagePreviewBottomSheet::class.java.name)
}

fun String.makeCapitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

val randomUUID
    get() = UUID.randomUUID().toString()
