package com.averin.android.developer.media.presentation

import android.net.Uri
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.base.util.FilePickerConst
import com.averin.android.developer.base.util.isGranted
import com.averin.android.developer.baseui.extension.android.app.pickFileByIntent
import com.averin.android.developer.baseui.extension.androidx.fragment.app.supportFragmentManager
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.media.navigation.MediaNavigation
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrMediaBinding
import org.koin.android.ext.android.inject
import java.io.File

class MediaFragment : BaseFragment(R.layout.fr_media) {
    private val binding by viewBinding(FrMediaBinding::bind)
    private val navigation: MediaNavigation by inject()
    override val viewModel: BaseViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            imagePicker.apply {
                requiredFragmentManager = supportFragmentManager()
                onPickImageClickAction = { chooseImageFromGallery() }
                imagePicker.onRemoveImageClickAction = { removeImage() }
            }
        }
    }

    override fun onImageSelected(imagePath: String?) {
        imagePath?.let { path ->
            val file = File(path)
            // Send the file to a Server, save to viewModel etc
            binding.imagePicker.imageUri = Uri.fromFile(file)
        }
    }

    protected fun chooseImageFromGallery() {
        if (!isGranted(requireActivity(), FilePickerConst.PERMISSIONS_FILE_PICKER)) {
            galleryPermissionResult.launch(FilePickerConst.PERMISSIONS_FILE_PICKER)
        } else {
            requireActivity().pickFileByIntent("image/*", photoFromGalleryLauncher)
        }
    }

    private fun removeImage() {
        // Remove from viewModel, Server etc
    }
}
