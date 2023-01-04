package com.averin.android.developer.baseui.widget.field

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import com.averin.android.developer.base.NO_VALUE_INT
import com.averin.android.developer.baseui.extension.android.content.hideKeyboard
import com.averin.android.developer.core.R

class CustomSelectView(c: Context, attrs: AttributeSet?) : BaseFieldView(c, attrs) {

    override val layoutRes: Int
        get() = R.layout.w_selectable

    override val isValid: Boolean
        get() = textValue?.isNotEmpty() == true

    private val selectableView: LinearLayout = fieldView.findViewById(R.id.ltSelectable)
    private val textView: TextView = fieldView.findViewById(R.id.tvSelectableTextView)
    private val leftIconView: ImageView = fieldView.findViewById(R.id.ivLeftAction)
    private val rightIconView: ImageView = fieldView.findViewById(R.id.ivRightAction)

    var onSelectViewListener: (() -> Unit)? = null

    var textValue: String? = null
        set(value) {
            field = value
            textView.text = value
        }
        get() = textView.text.toString()

    var labelValue: String = ""
        set(value) {
            field = value
            labelView.text = value
        }
        get() = labelView.text.toString()

    var hintValue: String? = null
        set(value) {
            field = value
            textView.hint = value
        }
        get() = textView.hint.toString()

    var leftActionIcon: Drawable? = null
        set(value) {
            field = value
            value?.let { leftIconView.setImageDrawable(it) }
            leftIconView.isVisible = value != null
        }

    var rightActionIcon: Drawable? = null
        set(value) {
            field = value
            value?.let { rightIconView.setImageDrawable(it) }
            rightIconView.isVisible = value != null
        }

    override fun focus() {
        selectableView.requestFocus()
    }

    override fun clear() {
        textValue = null
    }

    override fun validate() {
        val error: String? = if (textView.text.isNullOrEmpty()) {
            context.getString(R.string.field_is_required)
        } else {
            null
        }
        showError(error)
    }

    override fun setEnabled(isEnabled: Boolean) {
        super.setEnabled(isEnabled)
        selectableView.isEnabled = isEnabled
    }

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomSelectView, 0, 0)
        try {
            textValue = a.getString(R.styleable.CustomSelectView_cpsText) ?: ""
            labelValue = a.getString(R.styleable.CustomSelectView_cpsLabel) ?: ""
            hintValue = a.getString(R.styleable.CustomSelectView_cpsHint) ?: ""

            val leftActionResId = a.getResourceId(R.styleable.CustomSelectView_cpsActionIcon, NO_VALUE_INT)
            leftActionIcon = if (leftActionResId != NO_VALUE_INT) {
                AppCompatResources.getDrawable(context, leftActionResId)
            } else {
                null
            }

            val rightActionResId = a.getResourceId(R.styleable.CustomSelectView_cpsEndIcon, NO_VALUE_INT)
            rightActionIcon = if (rightActionResId != NO_VALUE_INT) {
                AppCompatResources.getDrawable(context, rightActionResId)
            } else {
                null
            }
        } finally {
            a.recycle()
        }
        selectableView.setOnClickListener {
            onSelectionChanged(true)
            postDelayed(100) {
                onFieldClicked()
                onSelectionChanged(false)
            }
        }
    }

    private fun onFieldClicked() {
        context.hideKeyboard(selectableView)
        showError(null)
        onSelectViewListener?.invoke()
    }
}
