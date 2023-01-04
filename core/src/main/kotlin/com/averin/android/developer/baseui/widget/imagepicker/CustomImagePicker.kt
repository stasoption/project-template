package com.averin.android.developer.baseui.widget.imagepicker

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.base.extension.java.io.showFullScreenImageUri
import com.averin.android.developer.base.extension.kotlin.showFullScreenImageUrl
import com.averin.android.developer.baseui.extension.android.view.loadImage
import com.averin.android.developer.baseui.extension.android.view.loadUri
import com.averin.android.developer.baseui.extension.android.view.setIsEnabled
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.WImagePickerBinding

class CustomImagePicker(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding by viewBinding(WImagePickerBinding::bind)
    private val isImageNonEmpty: Boolean
        get() = imageUri != null || imageUrl != null

    var requiredFragmentManager: FragmentManager? = null
    var onPickImageClickAction: (() -> Unit)? = null
    var onRemoveImageClickAction: (() -> Unit)? = null
    var placeholderRes: Int = R.drawable.ic_logo_placeholder
        set(value) {
            field = value
            binding.ivImagePreview.setImageResource(value)
        }

    // TODO: Error handler and icon
    var imageUri: Uri? = null
        set(value) {
            field = value
            value?.let { uri ->
                binding.ivImagePreview.loadUri(
                    uri = uri,
                    mask = R.drawable.bg_circle_mask,
                    placeholder = R.drawable.bg_circle_mask,
                    progressView = binding.imageLoader
                )
            }
            updateActionButton()
        }

    // TODO: Error handler and icon
    var imageUrl: String? = null
        set(value) {
            field = value
            value?.let { url ->
                binding.ivImagePreview.loadImage(
                    imageUrl = url,
                    mask = R.drawable.bg_circle_mask,
                    placeholder = R.drawable.bg_circle_mask,
                    progressView = binding.imageLoader
                )
            }
            updateActionButton()
        }

    var dialogTitle: String = ""

    override fun setEnabled(isEnabled: Boolean) {
        binding.ivImagePreview.setIsEnabled(isEnabled)
    }

    init {
        inflate(context, R.layout.w_image_picker, this)
        binding.ivImagePreview.setImageResource(placeholderRes)
        binding.ivImagePreview.setOnClickListener {
            if (context !is AppCompatActivity) {
                return@setOnClickListener
            }
            if (imageUri != null) {
                imageUri.showFullScreenImageUri(context as AppCompatActivity)
            } else if (imageUrl != null) {
                imageUrl.showFullScreenImageUrl(context as AppCompatActivity)
            }
        }
        binding.btnImageAction.setOnClickListener {
            if (isImageNonEmpty) {
                updateImage()
            } else {
                addImage()
            }
        }
    }

    private fun addImage() {
        onPickImageClickAction?.invoke()
    }

    private fun updateImage() {
        ImageActionsBottomSheet.newInstance(dialogTitle).apply {
            onClickImageActionListener = { action ->
                when (action) {
                    ImageActionsBottomSheet.Action.REMOVE -> {
                        removeImage()
                    }
                    ImageActionsBottomSheet.Action.UPDATE -> {
                        addImage()
                    }
                }
            }
        }.show(requiredFragmentManager!!, ImageActionsBottomSheet::class.java.name)
    }

    private fun removeImage() {
        binding.ivImagePreview.setImageResource(placeholderRes)
        imageUri = null
        imageUrl = null
        onRemoveImageClickAction?.invoke()
    }

    private fun updateActionButton() {
        val icon = if (isImageNonEmpty) {
            R.drawable.ic_edit_image
        } else {
            R.drawable.ic_add_image
        }
        binding.btnImageAction.setImageResource(icon)
    }
}
