package com.averin.android.developer.baseui.widget.imagepicker

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.presentation.bottomsheet.BaseBottomSheetDialog
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.FrImageActionBinding

class ImageActionsBottomSheet : BaseBottomSheetDialog() {

    private val binding by viewBinding(FrImageActionBinding::bind)
    override val layoutRes: Int = R.layout.fr_image_action

    var onClickImageActionListener: ((Action) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            tvTitle.text = arguments?.getString(ARG_TITLE)
            tvDelete.setOnClickListener {
                Action.REMOVE.select()
            }
            tvChange.setOnClickListener {
                Action.UPDATE.select()
            }
        }
    }

    private fun Action.select() {
        onClickImageActionListener?.invoke(this)
        dismissAllowingStateLoss()
    }

    enum class Action {
        REMOVE, UPDATE
    }

    companion object {
        private const val ARG_TITLE = "title"
        fun newInstance(title: String) = ImageActionsBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
            }
        }
    }
}
