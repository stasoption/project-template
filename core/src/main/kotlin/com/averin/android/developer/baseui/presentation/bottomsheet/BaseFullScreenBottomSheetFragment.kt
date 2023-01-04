package com.averin.android.developer.baseui.presentation.bottomsheet

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.error.ErrorView
import com.averin.android.developer.baseui.extension.android.view.doOnGlobalLayout
import com.averin.android.developer.baseui.extension.androidx.fragment.app.getColor
import com.averin.android.developer.baseui.extension.androidx.fragment.app.getDrawable
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.BaseErrorViewBinding
import com.averin.android.developer.core.databinding.FrBaseFullscreenBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseFullScreenBottomSheetFragment(
    private val layoutResId: Int? = null
) : BottomSheetDialogFragment() {
    open val expandOnOpen = true
    open val canBeHidden = true
    open val canceledOnTouchOutside = true
    open val shouldClipToOutline = true
    open val canDraggable = true
    open val backgroundColor = R.color.white
    open val backgroundDrawable = R.drawable.bg_bottomsheet

    var topOffset: Int = 0
        set(value) {
            field = value
            val params = binding.vModalContainer.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, value, 0, 0)
            binding.vModalContainer.layoutParams = params
        }

    private lateinit var binding: FrBaseFullscreenBottomSheetBinding
    protected val errorBinding by viewBinding(BaseErrorViewBinding::bind, R.id.errorInfoView)

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            // nothing
        }
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (!canBeHidden && newState == BottomSheetBehavior.STATE_DRAGGING) {
                onDialogDragging()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                onDialogHidden()
            }
        }
    }

    protected val errorView: ErrorView = object : ErrorView {
        override fun showHttpException(t: Throwable) {
            errorBinding.errorInfoView.titleView.text = t.message
            showErrorView()
        }
        override fun showNetworkError(endpoint: String?, t: Throwable) {
            errorBinding.errorInfoView.titleView.text = getString(R.string.error_network)
            showErrorView()
        }
        override fun showUnexpectedError(t: Throwable) {
            errorBinding.errorInfoView.titleView.text = t.message
            showErrorView()
        }
    }

    private fun showErrorView() {
        errorBinding.errorInfoView.isVisible = true
    }

    open fun hideErrorView() {
        errorBinding.errorInfoView.isVisible = false
    }

    open fun onDialogDragging() {
        // nothing to do by default
    }

    open fun onDialogHidden() {
        // nothing to do by default
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FrBaseFullscreenBottomSheetBinding.inflate(inflater)
        layoutResId?.let { inflater.inflate(it, binding.vModalContainer) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorBinding.run {
            errorInfoView.buttonView.onClickListener = {
                hideErrorView()
            }
        }

        binding.run {
            if (backgroundColor != R.color.white) {
                vDialogOutside.background.colorFilter = PorterDuffColorFilter(
                    getColor(backgroundColor),
                    PorterDuff.Mode.SRC
                )
            }

            if (backgroundDrawable != R.drawable.bg_bottomsheet) {
                vDialogOutside.background = getDrawable(backgroundDrawable)
            }

            if (shouldClipToOutline) {
                vModalContainer.clipToOutline = true
            }
        }
        view.doOnGlobalLayout { setupBottomSheet(it) }
    }

    override fun onDestroyView() {
        binding.vModalContainer.outlineProvider = null
        super.onDestroyView()
    }

    open fun setupBottomSheet(view: View) {
        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        bottomSheetBehavior = bottomSheetDialog.behavior.apply {
            if (expandOnOpen) {
                state = BottomSheetBehavior.STATE_EXPANDED
            }
            addBottomSheetCallback(bottomSheetCallback)
            peekHeight = view.height
            skipCollapsed = true
            isHideable = canBeHidden
            isDraggable = canDraggable
        }
        bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.run {
            updateLayoutParams<ViewGroup.LayoutParams> {
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
    }
}
