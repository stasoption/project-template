package com.averin.android.developer.media.presentation

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.androidx.fragment.app.supportFragmentManager
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.baseui.widget.LiveChartView
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrMediaBinding
import com.averin.android.developer.media.navigation.MediaNavigation
import com.averin.android.developer.videoplayer.VideoPlayerActivity
import org.koin.android.ext.android.inject
import java.io.File
import kotlin.random.Random

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

            appCompatSeekBar.apply {
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        liveChartView.updateChart(randomChartFeed())
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) { }
                    override fun onStopTrackingTouch(seekBar: SeekBar) { }
                })
            }
            liveChartView.updateChart(randomChartFeed())
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

    private fun randomChartFeed(): List<LiveChartView.ChartFeed> {
        return listOf(
            LiveChartView.ChartFeed("Assertiveness", Random.nextInt(5, 95).toFloat()),
            LiveChartView.ChartFeed("Compassionate", Random.nextInt(5, 95).toFloat()),
            LiveChartView.ChartFeed("Communicator", Random.nextInt(5, 95).toFloat()),
            LiveChartView.ChartFeed("Ethical", Random.nextInt(5, 95).toFloat()),
            LiveChartView.ChartFeed("Functions", Random.nextInt(5, 95).toFloat()),
            LiveChartView.ChartFeed("Generosity", Random.nextInt(5, 95).toFloat()),
            LiveChartView.ChartFeed("Intelligence", Random.nextInt(5, 95).toFloat())
        )
    }

    companion object {
        private const val TEST_VIDEO_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
    }
}
