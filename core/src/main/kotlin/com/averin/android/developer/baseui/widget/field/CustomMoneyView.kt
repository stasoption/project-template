package com.averin.android.developer.baseui.widget.field

import android.content.Context
import android.util.AttributeSet
import com.averin.android.developer.baseui.extension.android.content.showKeyboard
import com.averin.android.developer.baseui.util.validateNotEmpty
import com.averin.android.developer.core.R

class CustomMoneyView(c: Context, attrs: AttributeSet?) : BaseFieldView(c, attrs) {

    private var inputViewTextWatcher: MoneyTextWatcher? = null
    private val editTextView: CustomEditText = fieldView.findViewById(R.id.etTextValue)

    override val layoutRes: Int
        get() = R.layout.w_money_view

    override val isValid: Boolean
        get() = amountFormattedValue?.validateNotEmpty() == true

    var amountFormattedValue: String? = null
        set(value) {
            field = value
            MoneyTextWatcher.parseCurrencyValue(value)?.let { parsed ->
                editTextView.setText(parsed)
                editTextView.setSelection(parsed.length)
            }
        }
        get() = MoneyTextWatcher.parseCurrencyValue(editTextView.text.toString())

    var labelValue: String = ""
        set(value) {
            field = value
            labelView.text = value
        }
        get() = labelView.text.toString()

    var hintValue: String = ""
        set(value) {
            field = value
            editTextView.hint = value
        }
        get() = editTextView.hint.toString()

    private val errorMessage: String
        get() = resources.getString(R.string.field_is_required)

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomMoneyView, 0, 0)
        try {
            amountFormattedValue = a.getString(R.styleable.CustomMoneyView_cpmText)
            labelValue = a.getString(R.styleable.CustomMoneyView_cpmLabel) ?: ""
            hintValue = a.getString(R.styleable.CustomMoneyView_cpmHint) ?: ""
            isEnabled = a.getBoolean(R.styleable.CustomMoneyView_cpmIsEnable, true)
        } finally {
            a.recycle()
        }
        editTextView.filters = arrayOf(getMaxLengthFilter(MAX_LENGTH))
        editTextView.onFocusChangeListener = OnFocusChangeListener { _, isSelected ->
            onSelectionChanged(isSelected)
        }
    }

    override fun focus() {
        editTextView.requestFocus()
        context.showKeyboard(editTextView)
    }

    override fun clear() {
        editTextView.text = null
    }

    override fun validate() {
        val error: String? = if (isValid) {
            null
        } else {
            errorMessage
        }
        showError(error)
    }

    fun init(
        amount: String? = null,
        label: String? = null,
        hint: String? = null,
        onAmountChanged: ((source: Int?, formatted: String?) -> Unit)?
    ) {
        label?.let { labelValue = it }
        amount?.let { amountFormattedValue = it }
        hint?.let { hintValue = it }

        if (inputViewTextWatcher != null) {
            editTextView.removeTextChangedListener(inputViewTextWatcher)
            inputViewTextWatcher = null
        }

        inputViewTextWatcher = MoneyTextWatcher().apply {
            onAmountChangeListener = { source, formatted ->
                showError(null)
                onAmountChanged?.invoke(source, formatted)
                editTextView.removeTextChangedListener(this)
                editTextView.setText(formatted)
                editTextView.setSelection(formatted?.length ?: 0)
                editTextView.addTextChangedListener(this)
            }
        }
        editTextView.addTextChangedListener(inputViewTextWatcher)
    }

    companion object {
        private const val MAX_LENGTH = 11
    }
}
