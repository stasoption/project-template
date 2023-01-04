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
import com.averin.android.developer.core.databinding.WSegmentedButtonBinding

class CustomSegmentedView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var defaultTextColor: Int = getColor(R.color.gray)
    private var selectedTextColor: Int = getColor(R.color.typography_title)

    private val defaultBackground: Drawable = getDrawable(R.drawable.bg_segmented_inactive)
    private val selectedBackground: Drawable = getDrawable(R.drawable.bg_segmented_active)

    private val binding by viewBinding(WSegmentedButtonBinding::bind)
    private val leftBtnView: TextView by lazy { binding.tvLeftBtn }
    private val centralBtnView: TextView by lazy { binding.tvCentralBtn }
    private val rightBtnView: TextView by lazy { binding.tvRightBtn }

    var leftText: String? = null
        set(value) {
            field = value
            leftBtnView.text = value
            leftBtnView.isVisible = !value.isNullOrEmpty()
        }
    var centralText: String? = null
        set(value) {
            field = value
            centralBtnView.text = value
            centralBtnView.isVisible = !value.isNullOrEmpty()
        }
    var rightText: String? = null
        set(value) {
            field = value
            rightBtnView.text = value
            rightBtnView.isVisible = !value.isNullOrEmpty()
        }

    var onClickListener: ((Button) -> Unit)? = null

    var currentTubWithoutNotify: Button = Button.LEFT
        set(value) {
            field = value
            switchTab(
                button = field,
                callListenerAction = false
            )
        }

    init {
        inflate(context, R.layout.w_segmented_button, this)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomSegmentedView, 0, 0)
        try {
            leftText = typedArray.getString(R.styleable.CustomSegmentedView_cpsbLeftText)
            centralText = typedArray.getString(R.styleable.CustomSegmentedView_cpsbCentralText)
            rightText = typedArray.getString(R.styleable.CustomSegmentedView_cpsbRightText)

            leftBtnView.setOnClickListener { switchTab(Button.LEFT) }
            centralBtnView.setOnClickListener { switchTab(Button.CENTRAL) }
            rightBtnView.setOnClickListener { switchTab(Button.RIGHT) }

            // activate left button by default
            switchTab(Button.LEFT)
        } finally {
            typedArray.recycle()
        }
    }

    private fun switchTab(button: Button, callListenerAction: Boolean = true) {
        when (button) {
            Button.LEFT -> {
                leftBtnView.select(Button.LEFT, callListenerAction)
                centralBtnView.unselect()
                rightBtnView.unselect()
            }
            Button.CENTRAL -> {
                leftBtnView.unselect()
                centralBtnView.select(Button.CENTRAL, callListenerAction)
                rightBtnView.unselect()
            }
            Button.RIGHT -> {
                leftBtnView.unselect()
                centralBtnView.unselect()
                rightBtnView.select(Button.RIGHT, callListenerAction)
            }
        }
    }

    private fun TextView.select(button: Button, withClick: Boolean = true) {
        this.background = selectedBackground
        this.setTextColor(selectedTextColor)
        if (withClick) {
            onClickListener?.invoke(button)
        }
    }

    private fun TextView.unselect() {
        this.background = defaultBackground
        this.setTextColor(defaultTextColor)
    }

    enum class Button {
        LEFT, CENTRAL, RIGHT
    }
}
