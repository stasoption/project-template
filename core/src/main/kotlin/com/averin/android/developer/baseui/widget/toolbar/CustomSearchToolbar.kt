package com.averin.android.developer.baseui.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.widget.field.CustomSearchingView
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WSearchToolbarBinding
import timber.log.Timber

class CustomSearchToolbar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WSearchToolbarBinding::bind)
    private val searchInputView: CustomSearchingView by lazy { binding.searchInputView }
    private val tvScreenName: TextView by lazy { binding.tvScreenName }
    private val ivActionSearch: ImageView by lazy { binding.ivActionSearch }
    private val ivAction1: ImageView by lazy { binding.ivAction1 }
    private val ivAction2: ImageView by lazy { binding.ivAction2 }
    private val hideSearchBtn: ImageView by lazy { binding.ivHideSearch }
    private val hideClickableArea: View by lazy { binding.vBackClickableArea }

    private var withAction1: Boolean = false
    private var withAction2: Boolean = false

    var title: String = ""
        set(value) {
            field = value
            tvScreenName.text = title
        }

    @DrawableRes
    var action1Icon: Int = 0
        set(value) {
            field = value
            ivAction1.setImageResource(value)
        }

    @DrawableRes
    var action2Icon: Int = 0
        set(value) {
            field = value
            ivAction2.setImageResource(value)
        }

    var onQueryChangeListener: ((query: String) -> Unit)? = null
        set(value) {
            field = value
            searchInputView.onQueryChangeListener = value
        }

    var onClearClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            searchInputView.onClearClickListener = value
        }

    var onBackClickListener: (() -> Unit)? = null

    var isSearchingMode: Boolean = false
        set(value) {
            field = value
            tvScreenName.text = if (value) {
                context.getString(R.string.search)
            } else {
                title
            }
            searchInputView.isVisible = value
            hideSearchBtn.isVisible = value
            hideClickableArea.isVisible = value

            ivActionSearch.isVisible = !value
            ivAction1.isVisible = !value && withAction1
            ivAction2.isVisible = !value && withAction2

            post {
                if (value) {
                    searchInputView.focus()
                } else {
                    searchInputView.clear()
                }
            }
        }

    init {
        View.inflate(context, R.layout.w_search_toolbar, this)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomSearchToolbar, 0, 0)
        try {
            title = a.getString(R.styleable.CustomSearchToolbar_searchPanelTitle) ?: ""
            searchInputView.hint = a.getString(R.styleable.CustomSearchToolbar_searchPanelHint)

            ivActionSearch.setOnClickListener {
                isSearchingMode = true
            }
            hideClickableArea.setOnClickListener {
                isSearchingMode = false
                onBackClickListener?.invoke()
            }
            isSearchingMode = false
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            a.recycle()
        }
    }

    fun bindAction1(@DrawableRes actionIcon: Int, onClickListener: (() -> Unit)) {
        withAction1 = true
        ivAction1.isVisible = true
        ivAction1.setImageResource(actionIcon)
        ivAction1.setOnClickListener { onClickListener.invoke() }
    }

    fun removeAction1() {
        withAction1 = false
        ivAction1.isVisible = false
        ivAction1.setImageResource(0)
        ivAction1.setOnClickListener(null)
    }

    fun bindAction2(@DrawableRes actionIcon: Int, onClickListener: (() -> Unit)) {
        withAction2 = true
        ivAction2.isVisible = true
        ivAction2.setImageResource(actionIcon)
        ivAction2.setOnClickListener { onClickListener.invoke() }
    }

    fun removeAction2() {
        withAction2 = false
        ivAction2.isVisible = false
        ivAction2.setImageResource(0)
        ivAction2.setOnClickListener(null)
    }

    fun clear() {
        searchInputView.clear()
    }
}
