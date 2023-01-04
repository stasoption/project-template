package com.averin.android.developer.baseui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.base.NO_VALUE_INT
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.content.getColorStateListKtx
import com.averin.android.developer.baseui.extension.android.content.getDrawableKtx
import com.averin.android.developer.baseui.extension.android.view.setIsEnabled
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WCustomButtonBinding

class CustomButton(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WCustomButtonBinding::bind)
    private val rootView: LinearLayout by lazy { binding.ltButtonRoot }
    private val iconView: ImageView by lazy { binding.ivButtonIcon }
    private val textView: TextView by lazy { binding.tvButtonText }

    private val buttonBackground: Drawable?
        get() = when (buttonType) {
            Type.PRIMARY -> context.getDrawableKtx(R.drawable.bg_button_primary)
            Type.SECONDARY -> context.getDrawableKtx(R.drawable.bg_button_secondary)
            Type.WARNING -> context.getDrawableKtx(R.drawable.bg_button_remover)
        }

    private val buttonTextColor: ColorStateList
        get() = when (buttonType) {
            Type.PRIMARY -> context.getColorStateListKtx(R.color.selector_primary_button_text_color)
            Type.SECONDARY -> context.getColorStateListKtx(R.color.selector_secondary_button_text_color)
            Type.WARNING -> context.getColorStateListKtx(R.color.selector_warning_button_text_color)
        }

    private val buttonIconColor: Int
        get() = when (buttonType) {
            Type.PRIMARY -> context.getColorKtx(R.color.white)
            Type.SECONDARY -> if (isEnabled) {
                context.getColorKtx(R.color.textColorPrimary)
            } else {
                context.getColorKtx(R.color.light_gray)
            }
            Type.WARNING -> if (isEnabled) {
                context.getColorKtx(R.color.red)
            } else {
                context.getColorKtx(R.color.warning_button_disabled)
            }
        }

    var onClickListener: (() -> Unit)? = null

    var buttonText: String = ""
        set(value) {
            field = value
            textView.text = value
        }

    @DrawableRes
    var buttonIcon: Int = NO_VALUE_INT
        set(value) {
            field = value
            if (value != NO_VALUE_INT) {
                iconView.setImageDrawable(AppCompatResources.getDrawable(context, value))
            } else {
                iconView.setImageDrawable(null)
            }
            iconView.isVisible = value != NO_VALUE_INT
        }

    var buttonType: Type = Type.PRIMARY
        set(value) {
            field = value
            rootView.background = buttonBackground
            textView.setTextColor(buttonTextColor)
            iconView.setColorFilter(buttonIconColor, PorterDuff.Mode.SRC_IN)
        }

    init {
        inflate(context, R.layout.w_custom_button, this)
        bindAttributes(attrs)
    }

    private fun bindAttributes(attrs: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0)
        try {
            val iconResId = array.getResourceId(R.styleable.CustomButton_cpbIcon, NO_VALUE_INT)
            buttonIcon = iconResId

            buttonText = array.getString(R.styleable.CustomButton_cpbText) ?: ""

            val typeIndex = array.getInteger(R.styleable.CustomButton_cpbType, 0)
            buttonType = Type.values()[typeIndex]

            isEnabled = array.getBoolean(R.styleable.CustomButton_isButtonEnabled, true)
        } finally {
            array.recycle()
        }
    }

    override fun setEnabled(isEnabled: Boolean) {
        textView.isEnabled = isEnabled
        rootView.isEnabled = isEnabled
        rootView.isClickable = isEnabled
        rootView.isFocusable = isEnabled
        iconView.setIsEnabled(isEnabled)
        rootView.setOnClickListener {
            if (isEnabled) {
                onClickListener?.invoke()
            } else {
                null
            }
        }
    }

    enum class Type {
        PRIMARY,
        SECONDARY,
        WARNING
    }
}
