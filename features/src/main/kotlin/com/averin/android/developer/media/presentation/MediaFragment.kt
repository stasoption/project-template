package com.averin.android.developer.media.presentation

import android.net.Uri
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.androidx.fragment.app.supportFragmentManager
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.media.navigation.MediaNavigation
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrMediaBinding
import com.averin.android.developer.videoplayer.VideoPlayerActivity
import org.koin.android.ext.android.inject
import timber.log.Timber
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

            btnSelectFile.onClickListener = {
                chooseFileFromStorage()
            }

            btnOpenVideoPlayer.onClickListener = {
                val intent = VideoPlayerActivity.makeIntent(requireActivity(), TEST_VIDEO_URL)
                startActivity(intent)
            }
        }
    }

    override fun onImageSelected(imagePath: String?) {
        imagePath?.let { path ->
            val file = File(path)
            binding.imagePicker.imageUri = Uri.fromFile(file)
        }
    }

    override fun onFileSelected(filePath: String?) {
        filePath?.let { path ->
            val file = File(path)
            binding.tvSelectedFile.text = file.name
        }
    }

    private fun removeImage() {
        // Remove from viewModel, Server etc
    }

    companion object {
        private const val TEST_VIDEO_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    }
}
