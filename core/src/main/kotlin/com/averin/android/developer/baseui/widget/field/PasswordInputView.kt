package com.averin.android.developer.baseui.widget.field

import android.content.Context
import android.os.Build
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.averin.android.developer.baseui.extension.android.view.addTextChangedListener
import com.averin.android.developer.baseui.util.validatePassword
import com.averin.android.developer.core.R

class PasswordInputView(c: Context, attrs: AttributeSet?) : BaseFieldView(c, attrs) {

    private var onTextChangedListener: ((String) -> Unit)? = null
    private var inputViewTextWatcher: TextWatcher? = null
    private val editTextView: CustomEditText = fieldView.findViewById(R.id.etPasswordInputView)
    private val showHideButton: ImageView = fieldView.findViewById(R.id.ivShowHideBtn)

    override val layoutRes: Int
        get() = R.layout.w_password_view

    override val isValid: Boolean
        get() = textValue.validatePassword()

    var textValue: String = ""
        set(value) {
            field = value
            editTextView.setText(value)
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

    var isAutofill: Boolean = true
        @RequiresApi(Build.VERSION_CODES.O)
        set(value) {
            field = value
            val autofillValue = if (value) {
                View.IMPORTANT_FOR_AUTOFILL_YES
            } else {
                View.IMPORTANT_FOR_AUTOFILL_NO
            }
            editTextView.importantForAutofill = autofillValue
        }

    var isPasswordHidden: Boolean = false
        set(value) {
            field = value
            val method = if (value) {
                PasswordTransformationMethod.getInstance()
            } else {
                HideReturnsTransformationMethod.getInstance()
            }
            val icon = if (value) {
                R.drawable.ic_eye_opened
            } else {
                R.drawable.ic_eye_closed
            }
            editTextView.transformationMethod = method
            editTextView.setSelection(editTextView.length())
            showHideButton.setImageResource(icon)
        }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0)
        try {
            val text = a.getString(R.styleable.PasswordInputView_pivText) ?: ""
            if (text.isNotEmpty()) {
                textValue = text
            }
            val label = a.getString(R.styleable.PasswordInputView_pivLabel) ?: ""
            labelValue = if (label.isNotEmpty()) {
                label
            } else {
                context.getString(R.string.password)
            }
            hintValue = a.getString(R.styleable.PasswordInputView_pivHint)
                ?: context.getString(R.string.hint_your_password)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                isAutofill = a.getBoolean(R.styleable.PasswordInputView_pivAutofill, true)
            }
        } finally {
            a.recycle()
        }

        editTextView.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        editTextView.onFocusChangeListener = OnFocusChangeListener { _, isSelected ->
            onSelectionChanged(isSelected)
        }
        showHideButton.setOnClickListener { isPasswordHidden = !isPasswordHidden }
        isPasswordHidden = true
    }

    override fun focus() {
        editTextView.requestFocus()
    }

    override fun clear() {
        editTextView.text = null
    }

    override fun validate() {
        val error: String? = if (isValid) {
            null
        } else {
            context.getString(R.string.invalid_password)
        }
        showError(error)
    }

    fun init(onTextChanged: ((String) -> Unit)) {
        if (inputViewTextWatcher != null) {
            editTextView.removeTextChangedListener(inputViewTextWatcher)
            inputViewTextWatcher = null
        }
        onTextChangedListener = onTextChanged
        inputViewTextWatcher = editTextView.addTextChangedListener {
            onTextChanged { text, _, _, _ ->
                showError(null)
                onTextChangedListener?.invoke(text.toString())
            }
        }
    }
}
