package com.averin.android.developer.baseui.widget.toolbar

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.extension.android.view.setIsEnabled
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WToolbarBinding
import timber.log.Timber

class CustomToolbar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WToolbarBinding::bind)

    var title: String? = null
        set(value) {
            field = value
            binding.tvScreenName.text = value
        }

    var onBackClickListener: (() -> Unit)? = null

    @DrawableRes
    var actionIcon1Res: Int? = null
        set(value) {
            field = value
            binding.ivAction1.isVisible = value != null
            value?.let { binding.ivAction1.setImageResource(it) }
        }
    var onAction1ClickListener: (() -> Unit)? = null
    var isAction1Enabled: Boolean = true
        set(value) {
            field = value
            binding.ivAction1.setIsEnabled(value)
        }
    var actionIcon1Color: Int? = null
        set(value) {
            field = value
            value?.let { colorRes ->
                binding.ivAction1.colorFilter = PorterDuffColorFilter(
                    context.getColorKtx(colorRes),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }

    @DrawableRes
    var actionIcon2Res: Int? = null
        set(value) {
            field = value
            binding.ivAction2.isVisible = value != null
            value?.let { binding.ivAction2.setImageResource(it) }
        }
    var onAction2ClickListener: (() -> Unit)? = null
    var isAction2Enabled: Boolean = true
        set(value) {
            field = value
            binding.ivAction2.setIsEnabled(value)
        }

    @DrawableRes
    var actionIcon3Res: Int? = null
        set(value) {
            field = value
            binding.ivAction3.isVisible = value != null
            value?.let { binding.ivAction3.setImageResource(it) }
        }
    var onAction3ClickListener: (() -> Unit)? = null
    var isAction3Enabled: Boolean = true
        set(value) {
            field = value
            binding.ivAction3.setIsEnabled(value)
        }

    var linkText: String? = null
        set(value) {
            field = value
            binding.tvActionLink.isVisible = value != null
            value?.let { binding.tvActionLink.text = it }
        }
    var onLinkClickListener: (() -> Unit)? = null
    var isLinkEnabled: Boolean = true
        set(value) {
            field = value
            binding.tvActionLink.setIsEnabled(value)
        }

    init {
        View.inflate(context, R.layout.w_toolbar, this)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)
        try {
            title = a.getString(R.styleable.CustomToolbar_cptTitle)
            linkText = a.getString(R.styleable.CustomToolbar_cptLink)
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            a.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        binding.run {
            vBackClickableArea.setOnClickListener {
                onBackClickListener?.invoke()
            }
            ivAction1.setOnClickListener {
                onAction1ClickListener?.invoke()
            }
            ivAction2.setOnClickListener {
                onAction2ClickListener?.invoke()
            }
            ivAction3.setOnClickListener {
                onAction3ClickListener?.invoke()
            }
            tvActionLink.setOnClickListener {
                onLinkClickListener?.invoke()
            }
        }
    }
}
