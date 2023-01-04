package com.averin.android.developer.baseui.widget.field

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import com.averin.android.developer.baseui.extension.android.content.showKeyboard
import com.averin.android.developer.baseui.extension.android.view.addTextChangedListener
import com.averin.android.developer.baseui.util.MAX_SMS_LENGTH
import com.averin.android.developer.baseui.util.MAX_TEXT_LENGTH
import com.averin.android.developer.baseui.util.VERIFICATION_CODE_LENGTH
import com.averin.android.developer.baseui.util.validateMail
import com.averin.android.developer.baseui.util.validateNotEmpty
import com.averin.android.developer.core.R

class CustomInputView(c: Context, attrs: AttributeSet?) : BaseFieldView(c, attrs) {

    private var onTextChangedListener: ((String) -> Unit)? = null
    private var inputViewTextWatcher: TextWatcher? = null
    private val editTextView: CustomEditText = fieldView.findViewById(R.id.etTextValue)

    override val layoutRes: Int
        get() = R.layout.w_input_view

    override val isValid: Boolean
        get() = when (type) {
            Type.EMAIL -> textValue.validateMail()
            else -> textValue.validateNotEmpty()
        }

    private val filters: MutableList<InputFilter> = mutableListOf()

    var textValue: String = ""
        set(value) {
            field = value
            editTextView.setText(value)
            editTextView.setSelection(value.length)
        }
        get() = editTextView.text.toString()

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

    var type: Type = Type.TEXT
        set(value) {
            field = value
            editTextView.inputType = when (value) {
                Type.TEXT -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                Type.PERSONAL_NAME -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME or InputType.TYPE_TEXT_FLAG_CAP_WORDS
                Type.COMPANY_NAME -> InputType.TYPE_TEXT_VARIATION_PERSON_NAME or InputType.TYPE_TEXT_FLAG_CAP_WORDS
                Type.EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                Type.NUMBER -> InputType.TYPE_CLASS_NUMBER
                Type.SMS -> InputType.TYPE_CLASS_TEXT
                Type.CONFIRM_CODE -> InputType.TYPE_CLASS_NUMBER
            }
            when (value) {
                Type.COMPANY_NAME -> {
                    filters.add(getAsciiFilter())
                }
                Type.SMS -> {
                    filters.add(getMaxLengthFilter(MAX_SMS_LENGTH))
                }
                Type.CONFIRM_CODE -> {
                    filters.add(getMaxLengthFilter(VERIFICATION_CODE_LENGTH))
                }
                else -> {
                    filters.add(getMaxLengthFilter(MAX_TEXT_LENGTH))
                }
            }
            editTextView.filters = filters.toTypedArray()
        }

    var imeOptions: Int = EditorInfo.IME_ACTION_DONE
        set(value) {
            field = value
            editTextView.imeOptions = imeOptions
        }

    var maxSymbols = DEFAULT_MAX_SYMBOLS_NUMBER
        set(value) {
            field = value
            filters.add(getMaxLengthFilter(value))
            editTextView.filters = filters.toTypedArray()
            symbolsCounterView.text = context.getString(R.string.symbols_counter, textValue.length, maxSymbols)
        }

    private val errorMessage: String
        get() = when (type) {
            Type.EMAIL -> resources.getString(R.string.invalid_email_error)
            else -> resources.getString(R.string.field_is_required)
        }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomInputView, 0, 0)
        try {
            textValue = a.getString(R.styleable.CustomInputView_cpiText) ?: ""
            labelValue = a.getString(R.styleable.CustomInputView_cpiLabel) ?: ""
            hintValue = a.getString(R.styleable.CustomInputView_cpiHint) ?: ""
            isEnabled = a.getBoolean(R.styleable.CustomInputView_cpiIsEnable, true)
            val valueType = a.getInteger(R.styleable.CustomInputView_cpiInputType, 0)
            type = Type.values()[valueType]
        } finally {
            a.recycle()
        }
        editTextView.onFocusChangeListener = OnFocusChangeListener { _, isSelected ->
            onSelectionChanged(isSelected)
        }
    }

    fun bindSymbolCounter(maxTextSymbols: Int = DEFAULT_MAX_SYMBOLS_NUMBER) {
        maxSymbols = maxTextSymbols
        symbolsCounterView.isVisible = true
        symbolsCounterView.text = context.getString(R.string.symbols_counter, textValue.length, maxSymbols)
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
        text: String? = null,
        label: String? = null,
        hint: String? = null,
        onTextChanged: ((String) -> Unit)?
    ) {
        label?.let { labelValue = it }
        text?.let { textValue = it }
        hint?.let { hintValue = it }

        if (inputViewTextWatcher != null) {
            editTextView.removeTextChangedListener(inputViewTextWatcher)
            inputViewTextWatcher = null
        }

        onTextChangedListener = onTextChanged
        inputViewTextWatcher = editTextView.addTextChangedListener {
            onTextChanged { text, _, _, _ ->
                showError(null)
                onTextChangedListener?.invoke(text.toString())
                if (symbolsCounterView.isVisible) {
                    symbolsCounterView.text = context.getString(R.string.symbols_counter, text?.length ?: 0, maxSymbols)
                }
            }
        }
    }

    fun setOnEditorActionListener(
        editorInfo: Int = EditorInfo.IME_ACTION_DONE,
        onKeyEvent: (() -> Unit)
    ) {
        imeOptions = editorInfo
        editTextView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == editorInfo) {
                onKeyEvent.invoke()
                true
            } else {
                false
            }
        }
    }

    enum class Type {
        TEXT,
        PERSONAL_NAME,
        COMPANY_NAME,
        EMAIL,
        NUMBER,
        SMS,
        CONFIRM_CODE
    }

    companion object {
        private const val DEFAULT_MAX_SYMBOLS_NUMBER = 55
    }
}
