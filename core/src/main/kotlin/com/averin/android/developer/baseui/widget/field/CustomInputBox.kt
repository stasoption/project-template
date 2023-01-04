package com.averin.android.developer.baseui.widget.field

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.view.isInvisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.content.hideKeyboard
import com.averin.android.developer.baseui.extension.android.content.showKeyboard
import com.averin.android.developer.baseui.extension.android.view.addTextChangedListener
import com.averin.android.developer.baseui.extension.android.view.setCompoundDrawable
import com.averin.android.developer.baseui.extension.android.view.setIsEnabled
import com.averin.android.developer.baseui.util.validateNotEmpty
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WInputBoxBinding

class CustomInputBox(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), ValidatableField {

    private val binding by viewBinding(WInputBoxBinding::bind)
    private val inputBoxView: ViewGroup by lazy { binding.ltInputBox }
    private val labelView: TextView by lazy { binding.tvLabel }
    private val editTextView: CustomEditText by lazy { binding.etTextValue }
    private val symbolsCounterView: TextView by lazy { binding.tvSymbolsCounter }
    private val errorMessageView: TextView by lazy { binding.tvErrorMessage }

    private var defaultBackground = R.drawable.bg_input_view
    private var errorBackground = R.drawable.bg_input_view_error
    private var disabledBackground = R.drawable.bg_input_view_disabled

    private var onTextChangedListener: ((String) -> Unit)? = null
    private var inputViewTextWatcher: TextWatcher? = null

    var textValue: String = ""
        set(value) {
            field = value
            editTextView.setText(value)
            moveCursorToEnd()
        }
        get() = editTextView.text.toString()

    var hintValue: String = ""
        set(value) {
            field = value
            editTextView.hint = value
        }
        get() = editTextView.hint.toString()

    var labelValue: String = ""
        set(value) {
            field = value
            labelView.text = value
        }
        get() = labelView.text.toString()

    var minLines = MIN_LINES_DEFAULT
        set(value) {
            field = value
            editTextView.minLines = value
        }

    var maxLines = MAX_LINES_DEFAULT
        set(value) {
            field = value
            editTextView.maxLines = value
        }

    var maxSymbols = DEFAULT_MAX_SYMBOLS_NUMBER
        set(value) {
            field = value
            editTextView.filters = arrayOf(getMaxLengthFilter(value))
            symbolsCounterView.text = context.getString(R.string.symbols_counter, textValue.length, maxSymbols)
        }

    val isUnderFocus: Boolean
        get() = editTextView.isFocused

    override val isValid: Boolean
        get() = textValue.validateNotEmpty()

    init {
        inflate(context, R.layout.w_input_box, this)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomInputBox, 0, 0)
        try {
            textValue = a.getString(R.styleable.CustomInputBox_cpibText) ?: ""
            labelValue = a.getString(R.styleable.CustomInputBox_cpibLabel) ?: ""
            hintValue = a.getString(R.styleable.CustomInputBox_cpibHint) ?: ""
            minLines = a.getInt(R.styleable.CustomInputBox_cpibMinLines, MIN_LINES_DEFAULT)
            maxLines = a.getInt(R.styleable.CustomInputBox_cpibMaxLines, MAX_LINES_DEFAULT)
            maxSymbols = a.getInt(R.styleable.CustomInputBox_cpibMaxSymbols, DEFAULT_MAX_SYMBOLS_NUMBER)
            isEnabled = a.getBoolean(R.styleable.CustomInputBox_cpibIsEnable, true)
        } finally {
            a.recycle()
        }
        editTextView.onFocusChangeListener = OnFocusChangeListener { _, isSelected ->
            onSelectionChanged(isSelected)
        }
    }

    @CallSuper
    override fun setEnabled(isEnabled: Boolean) {
        editTextView.setIsEnabled(isEnabled)

        val drawableRes = if (isEnabled) {
            defaultBackground
        } else {
            disabledBackground
        }
        editTextView.setBackgroundResource(drawableRes)
    }

    fun focus(isFocus: Boolean) {
        if (isFocus) {
            editTextView.requestFocus()
            context.showKeyboard(editTextView)
        } else {
            inputBoxView.requestFocus()
            context.hideKeyboard(editTextView)
        }
    }

    fun clear() {
        editTextView.text = null
    }

    fun moveCursorToEnd() {
        val endPosition = editTextView.text?.length ?: 0
        editTextView.setSelection(endPosition)
    }

    fun bindLabelSuggestion(
        iconId: Int = R.drawable.ic_suggestion,
        onClickAction: () -> Unit
    ) {
        labelView.setCompoundDrawable(endIconId = iconId)
        labelView.setOnClickListener { onClickAction.invoke() }
    }

    fun init(
        text: String? = null,
        label: String? = null,
        onTextChanged: ((String) -> Unit)?
    ) {
        label?.let { labelValue = it }
        text?.let { textValue = it }

        if (inputViewTextWatcher != null) {
            editTextView.removeTextChangedListener(inputViewTextWatcher)
            inputViewTextWatcher = null
        }
        onTextChangedListener = onTextChanged
        symbolsCounterView.text = context.getString(R.string.symbols_counter, textValue.length, maxSymbols)
        inputViewTextWatcher = editTextView.addTextChangedListener {
            onTextChanged { text, _, _, _ ->
                showError(null)
                symbolsCounterView.text = context.getString(R.string.symbols_counter, text?.length ?: 0, maxSymbols)
                onTextChangedListener?.invoke(text.toString())
            }
        }
    }

    private fun onSelectionChanged(isSelect: Boolean) {
        labelView.isActivated = isSelect
        editTextView.isActivated = isSelect
    }

    override fun validate() {
        val error: String? = if (isValid) {
            null
        } else {
            resources.getString(R.string.field_is_required)
        }
        showError(error)
    }

    @CallSuper
    override fun showError(message: String?) {
        val drawableRes = if (message == null) {
            defaultBackground
        } else {
            errorBackground
        }
        if (message == null) {
            labelView.setTextColor(context.getColorKtx(R.color.textColorPrimary))
        } else {
            labelView.setTextColor(context.getColorKtx(R.color.error))
        }
        editTextView.setBackgroundResource(drawableRes)
        errorMessageView.text = message
        errorMessageView.isInvisible = message == null
    }

    companion object {
        private const val DEFAULT_MAX_SYMBOLS_NUMBER = 1000
        private const val MIN_LINES_DEFAULT = 3
        private const val MAX_LINES_DEFAULT = 10
    }
}
