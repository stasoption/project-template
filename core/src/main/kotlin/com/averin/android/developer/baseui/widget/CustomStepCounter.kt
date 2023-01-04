package com.averin.android.developer.baseui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.base.extension.kotlin.toDP
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.content.getDrawableKtx
import com.averin.android.developer.baseui.extension.android.view.setBackgroundTintColor
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WStepCounterBinding

class CustomStepCounter(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WStepCounterBinding::bind)
    private val ltStepsContainer: LinearLayout by lazy { binding.ltStepsContainer }

    private var steps: MutableList<Step> = mutableListOf()

    var stepNumbers: Int = 0
        set(value) {
            field = value
            steps.clear()
            ltStepsContainer.removeAllViews()
            for (i in 0 until value) {
                val step = Step(i)
                steps.add(step)
                ltStepsContainer.addView(step)
            }
        }

    var currentStep: Int = 0
        set(value) {
            field = value
            steps.forEach { it.updateAppearance() }
        }

    init {
        inflate(context, R.layout.w_step_counter, this)
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomStepCounter, 0, 0)
        try {
            stepNumbers = array.getInteger(R.styleable.CustomStepCounter_stepNumbers, 3)
            currentStep = array.getInteger(R.styleable.CustomStepCounter_currentStep, 0)
        } finally {
            array.recycle()
        }
    }

    inner class Step(private val stepNumber: Int, context: Context = getContext()) : View(context) {

        private val isPrevOrCurrentStep: Boolean
            get() = stepNumber <= currentStep

        init {
            background = context.getDrawableKtx(R.drawable.bg_view)
            transformForStep()
            updateAppearance()
        }

        private fun transformForStep() {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                4.toDP,
                1f
            )

            val leftMargin = if (stepNumber == 0) {
                0.toDP
            } else {
                8.toDP
            }

            val rightMargin = if (stepNumber == steps.size) {
                0.toDP
            } else {
                8.toDP
            }

            params.setMargins(leftMargin, 0, rightMargin, 0)
            this.layoutParams = params
        }

        fun updateAppearance() {
            val bgColorId = if (isPrevOrCurrentStep) {
                R.color.colorPrimary
            } else {
                R.color.light_gray
            }
            this.setBackgroundTintColor(context.getColorKtx(bgColorId))
        }
    }
}
