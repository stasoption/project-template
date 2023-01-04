package com.averin.android.developer.baseui.widget.field

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.view.setCompoundDrawable
import com.averin.android.developer.baseui.extension.android.view.setDrawableTintColor
import com.averin.android.developer.baseui.extension.android.view.setIsEnabled
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WBaseFieldViewBinding

abstract class BaseFieldView(c: Context, a: AttributeSet?) : FrameLayout(c, a), ValidatableField {

    abstract val layoutRes: Int

    abstract fun focus()

    abstract fun clear()

    private var defaultBackground = R.drawable.bg_input_view
    private var disabledBackground = R.drawable.bg_input_view_disabled
    private var errorBackground = R.drawable.bg_input_view_error

    private val binding by viewBinding(WBaseFieldViewBinding::bind)
    private val containerView: ViewGroup by lazy { binding.ltContainerView }
    private val errorMessageView: TextView by lazy { binding.tvErrorMessage }
    protected val symbolsCounterView: TextView by lazy { binding.tvSymbolsCounter }
    protected val labelView: TextView by lazy { binding.tvLabel }
    protected val fieldView: View by lazy { LayoutInflater.from(context).inflate(layoutRes, null) }

    var onFieldFocusChangeListener: ((isFocus: Boolean) -> Unit)? = null

    var isWithLabel: Boolean = true
        set(value) {
            field = value
            labelView.isVisible = value
        }

    init {
        inflate(context, R.layout.w_base_field_view, this)
        containerView.removeAllViews()
        containerView.addView(fieldView)
    }

    @CallSuper
    override fun setEnabled(isEnabled: Boolean) {
        fieldView.setIsEnabled(isEnabled)

        val drawableRes = if (isEnabled) {
            defaultBackground
        } else {
            disabledBackground
        }
        containerView.setBackgroundResource(drawableRes)
    }

    @CallSuper
    open fun onSelectionChanged(isSelect: Boolean) {
        if (!isEnabled) return
        labelView.isActivated = isSelect
        containerView.isActivated = isSelect
        onFieldFocusChangeListener?.invoke(isSelect)
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
            labelView.setDrawableTintColor(context.getColorKtx(R.color.textColorPrimary))
        } else {
            labelView.setTextColor(context.getColorKtx(R.color.error))
            labelView.setDrawableTintColor(context.getColorKtx(R.color.error))
        }
        containerView.setBackgroundResource(drawableRes)
        errorMessageView.text = message
        errorMessageView.isInvisible = message == null
    }

    fun bindLabelSuggestion(
        iconId: Int = R.drawable.ic_suggestion,
        onClickAction: () -> Unit
    ) {
        labelView.setCompoundDrawable(endIconId = iconId)
        labelView.setOnClickListener { onClickAction.invoke() }
    }
}
