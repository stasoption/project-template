package com.averin.android.developer.baseui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.BaseInfoViewBinding

class InfoView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val binding by viewBinding(BaseInfoViewBinding::bind)

    val iconView: ImageView by lazy { binding.imgInfo }
    val titleView: TextView by lazy { binding.tvInfo }
    val buttonView: CustomButton by lazy { binding.btnInfo }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        setBackgroundResource(R.color.window_background)

        inflate(context, R.layout.base_info_view, this)
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.InfoView,
            0, 0
        )

        try {
            val resourceId = a.getResourceId(R.styleable.InfoView_icon, 0)
            if (resourceId != 0) iconView.setImageResource(resourceId)
            titleView.text = a.getString(R.styleable.InfoView_titleText)
            buttonView.buttonText = a.getString(R.styleable.InfoView_buttonText) ?: ""
        } finally {
            a.recycle()
        }
    }
}
