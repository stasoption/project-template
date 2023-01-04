package com.averin.android.developer.baseui.widget.field

import android.text.Editable
import android.text.TextWatcher
import com.averin.android.developer.base.extension.kotlin.removeNoDigits
import com.averin.android.developer.base.extension.kotlin.toAmountFormat

internal class MoneyTextWatcher : TextWatcher {

    var onAmountChangeListener: ((sourceAmount: Int?, formattedAmount: String?) -> Unit)? = null

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        val value = s.toString().removeNoDigits()?.ifEmpty { null }
        val sourceAmount = value?.toInt()
        val formattedAmount = parseCurrencyValue(value)
        onAmountChangeListener?.invoke(sourceAmount, formattedAmount)
    }

    companion object {
        fun parseCurrencyValue(value: String?): String? {
            value ?: return null
            return value.removeNoDigits()?.toInt()?.toAmountFormat()
        }
    }
}
