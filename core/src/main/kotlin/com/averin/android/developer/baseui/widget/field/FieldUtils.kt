package com.averin.android.developer.baseui.widget.field

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Matcher
import java.util.regex.Pattern

fun getAsciiFilter(): InputFilter = AsciiFilterBuilder()

fun getMaxLengthFilter(
    maxLength: Int
): InputFilter = LengthFilterBuilder()
    .maxLength(maxLength)
    .build()

class AsciiFilterBuilder : InputFilter {
    private val condition = "^[\\u0000-\\u007F]*\$"
    private val regex = condition.toRegex()
    private val pattern = Pattern.compile(condition)
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        return source.toCheckedCharSequence()
    }

    private fun CharSequence.toCheckedCharSequence(): CharSequence? {
        val matcher: Matcher = pattern.matcher(this)
        return if (!matcher.matches()) {
            val stringBuilder = StringBuilder()
            this.forEach { char ->
                if (regex.matches(char.toString())) {
                    stringBuilder.append(char)
                }
            }
            stringBuilder
        } else {
            null
        }
    }
}

class LengthFilterBuilder {
    private var maxLength: Int = 0
    fun maxLength(value: Int): LengthFilterBuilder {
        maxLength = value
        return this
    }
    fun build(): InputFilter {
        return InputFilter.LengthFilter(maxLength)
    }
}
