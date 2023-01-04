package com.averin.android.developer.baseui.widget.field

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.hideKeyboard
import com.averin.android.developer.baseui.extension.android.content.showKeyboard
import com.averin.android.developer.baseui.extension.android.view.addTextChangedListener
import com.averin.android.developer.baseui.extension.android.view.setIsEnabled
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WSearchViewBinding

class CustomSearchingView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WSearchViewBinding::bind)
    private val rootLayout: LinearLayout by lazy { binding.rootLayout }
    private val searchView: CustomEditText by lazy { binding.etSearchView }
    private val buttonClear: ImageView by lazy { binding.clearBtn }

    private var defaultBackground = R.drawable.bg_input_view
    private var disabledBackground = R.drawable.bg_input_view_disabled

    var onQueryChangeListener: ((query: String) -> Unit)? = null
    var onSearchClickListener: ((query: String) -> Unit)? = null
    var onClearClickListener: (() -> Unit)? = null
    var onFocusChangeListener: ((isHasFocus: Boolean) -> Unit)? = null

    var query: String
        set(value) {
            value.let { searchView.setText(it) }
        }
        get() {
            return searchView.text.toString()
        }

    var hint: String? = null
        set(value) {
            field = value
            searchView.hint = value
        }

    init {
        View.inflate(context, R.layout.w_search_view, this)

        searchView.addTextChangedListener {
            onTextChanged { text, _, _, _ ->
                onQueryChangeListener?.invoke(text.toString())
                checkActionButton(text?.isNotEmpty() == true)
            }
        }
        searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && query.isNotEmpty()) {
                onSearchClickListener?.invoke(query)
                context.hideKeyboard(searchView)
                true
            } else {
                context.hideKeyboard(searchView)
                false
            }
        }
        searchView.onFocusChangeListener = OnFocusChangeListener { _, isSelected ->
            onFocusChangeListener?.invoke(isSelected)
            rootLayout.isActivated = isSelected
            checkActionButton(isSelected)
        }
        checkActionButton(searchView.text?.isNotEmpty() == true)
        buttonClear.setOnClickListener { clear() }
    }

    @CallSuper
    override fun setEnabled(isEnabled: Boolean) {
        rootLayout.setIsEnabled(isEnabled)
        val drawableRes = if (isEnabled) {
            defaultBackground
        } else {
            disabledBackground
        }
        rootLayout.setBackgroundResource(drawableRes)
    }

    private fun checkActionButton(isNeedClear: Boolean) {
        buttonClear.isVisible = isNeedClear
    }

    fun focus() {
        searchView.requestFocus()
        context.showKeyboard(searchView)
    }
    fun clear() {
        searchView.text = null
        rootLayout.requestFocus()
        context.hideKeyboard(searchView)
        onClearClickListener?.invoke()
    }
}
