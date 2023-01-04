package com.averin.android.developer.baseui.widget.imagepicker

import android.net.Uri
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.view.loadImage
import com.averin.android.developer.baseui.extension.android.view.loadUri
import com.averin.android.developer.baseui.presentation.bottomsheet.BaseFullScreenBottomSheetFragment
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.FrImagePreviewBinding

class ImagePreviewBottomSheet : BaseFullScreenBottomSheetFragment(R.layout.fr_image_preview) {

    private val binding by viewBinding(FrImagePreviewBinding::bind)

    private val imageUrl: String? by lazy { arguments?.getString(ARG_IMAGE_URL) }
    private val imageUri: Uri? by lazy { arguments?.getParcelable(ARG_IMAGE_URI) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            if (imageUrl != null) {
                binding.ivImagePreview.loadImage(
                    imageUrl = imageUrl,
                    placeholder = R.drawable.image_placeholder,
                    progressView = binding.imageLoader
                )
            }
            if (imageUri != null) {
                binding.ivImagePreview.loadUri(
                    uri = imageUri!!,
                    mask = null,
                    placeholder = R.drawable.image_placeholder,
                    progressView = binding.imageLoader
                )
            }
        }
    }

    companion object {
        private const val ARG_IMAGE_URL = "ARG_IMAGE_URL"
        private const val ARG_IMAGE_URI = "ARG_IMAGE_URI"

        fun loadImageUrl(url: String) = ImagePreviewBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_IMAGE_URL, url)
            }
        }
        fun loadImageUri(uri: Uri) = ImagePreviewBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_IMAGE_URI, uri)
            }
        }
    }
}
