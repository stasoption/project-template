package com.averin.android.developer.baseui.widget.field

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.view.doOnGlobalLayout
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WExpandedTextViewBinding

class ExpandedTextView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WExpandedTextViewBinding::bind)
    private val expandedTextView: TextView by lazy { binding.tvExpandedTextView }
    private val showMoreBtn: TextView by lazy { binding.tvShowMoreBtn }

    private var isExpanded: Boolean = false
        set(value) {
            field = value
            if (value) {
                expandedTextView.expandText()
            } else {
                expandedTextView.collapseText()
            }
        }

    var text: String? = null
        set(value) {
            field = value
            expandedTextView.doOnGlobalLayout {
                expandedTextView.text = value
                showMoreBtn.isVisible = expandedTextView.lineCount >= MIN_LINES
            }
        }

    init {
        inflate(context, R.layout.w_expanded_text_view, this)
        showMoreBtn.setOnClickListener { isExpanded = !isExpanded }
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.ExpandedTextView, 0, 0)
        try {
            val attrText = array.getString(R.styleable.ExpandedTextView_etvText)
            if (!attrText.isNullOrEmpty()) {
                text = attrText
            }
            isExpanded = array.getBoolean(R.styleable.ExpandedTextView_isExpanded, false)
        } finally {
            array.recycle()
        }
    }

    private fun TextView.expandText() {
        this.maxLines = MAX_LINES
        this.ellipsize = null
        showMoreBtn.text = context.getString(R.string.collapse)
    }

    private fun TextView.collapseText() {
        this.maxLines = MIN_LINES
        this.ellipsize = TextUtils.TruncateAt.END
        showMoreBtn.text = context.getString(R.string.show_more)
    }

    companion object {
        private const val MIN_LINES = 4
        private const val MAX_LINES = Int.MAX_VALUE
    }
}
