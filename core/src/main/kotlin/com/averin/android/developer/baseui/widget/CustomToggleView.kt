package com.averin.android.developer.baseui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.view.getColor
import com.averin.android.developer.baseui.extension.android.view.getDrawable
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WCustomToggleBinding

class CustomToggleView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var defaultTextColor: Int = getColor(R.color.typography_title)
    private var selectedTextColor: Int = getColor(R.color.colorPrimary)
    private val defaultBackground: Drawable = getDrawable(R.drawable.bg_toggle_inactive)
    private val selectedBackground: Drawable = getDrawable(R.drawable.bg_toggle_active)

    private val binding by viewBinding(WCustomToggleBinding::bind)
    private val leftBtnView: TextView by lazy { binding.tvLeftBtn }
    private val centralBtnView: TextView by lazy { binding.tvCentralBtn }
    private val rightBtnView: TextView by lazy { binding.tvRightBtn }

    var onClickListener: ((Button) -> Unit)? = null

    var currentToggle: Button = Button.LEFT
        set(value) {
            field = value
            switchToggle(field)
        }

    init {
        inflate(context, R.layout.w_custom_toggle, this)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomToggleView, 0, 0)
        try {
            val leftText = typedArray.getString(R.styleable.CustomToggleView_toggleLeftText)
            val centralText = typedArray.getString(R.styleable.CustomToggleView_toggleCentralText)
            val rightText = typedArray.getString(R.styleable.CustomToggleView_toggleRightText)

            leftBtnView.text = leftText
            leftBtnView.isVisible = !leftText.isNullOrEmpty()
            centralBtnView.text = centralText
            centralBtnView.isVisible = !centralText.isNullOrEmpty()
            rightBtnView.text = rightText
            rightBtnView.isVisible = !rightText.isNullOrEmpty()

            leftBtnView.setOnClickListener { switchToggle(Button.LEFT) }
            centralBtnView.setOnClickListener { switchToggle(Button.CENTRAL) }
            rightBtnView.setOnClickListener { switchToggle(Button.RIGHT) }
        } finally {
            typedArray.recycle()
        }
    }

    private fun switchToggle(button: Button) {
        when (button) {
            Button.LEFT -> {
                leftBtnView.select(Button.LEFT)
                centralBtnView.unselect()
                rightBtnView.unselect()
            }
            Button.CENTRAL -> {
                leftBtnView.unselect()
                centralBtnView.select(Button.CENTRAL)
                rightBtnView.unselect()
            }
            Button.RIGHT -> {
                leftBtnView.unselect()
                centralBtnView.unselect()
                rightBtnView.select(Button.RIGHT)
            }
        }
    }

    private fun TextView.select(button: Button) {
        this.background = selectedBackground
        this.setTextColor(selectedTextColor)
        onClickListener?.invoke(button)
    }

    private fun TextView.unselect() {
        this.background = defaultBackground
        this.setTextColor(defaultTextColor)
    }

    enum class Button {
        LEFT, CENTRAL, RIGHT
    }
}
