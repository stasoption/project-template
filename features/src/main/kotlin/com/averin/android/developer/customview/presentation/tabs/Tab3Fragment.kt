package com.averin.android.developer.customview.presentation.tabs

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.showToast
import com.averin.android.developer.baseui.extension.androidx.fragment.app.hideKeyBoard
import com.averin.android.developer.baseui.extension.androidx.fragment.app.supportFragmentManager
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.baseui.widget.CustomSegmentedView
import com.averin.android.developer.baseui.widget.CustomToggleView
import com.averin.android.developer.baseui.widget.tag.Tag
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrTab3Binding
import java.io.File

class Tab3Fragment : BaseFragment(R.layout.fr_tab_3) {

    override val viewModel: BaseViewModel? = null
    private val binding by viewBinding(FrTab3Binding::bind)

    private val tagsMock = listOf<Tag>(
        Tag(1, "Assertiveness"),
        Tag(2, "Compassionate"),
        Tag(3, "Effective communicator"),
        Tag(4, "Ethical"),
        Tag(5, "Functions well under pressure"),
        Tag(6, "Generosity"),
        Tag(7, "Good attitude"),
        Tag(8, "High emotional intelligence")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            /*
      * SearchToolbar
      * */
            searchToolbar.apply {
                onQueryChangeListener = { }
                onClearClickListener = { }
                bindAction1(R.drawable.ic_settings) { }
                bindAction2(R.drawable.ic_filter_off) { }
            }

            /*
            * SegmentedButtonsView
            * */
            segmentedButtonView.onClickListener = { button ->
                hideKeyBoard()
                when (button) {
                    CustomSegmentedView.Button.LEFT -> {
                        context?.showToast(CustomSegmentedView.Button.LEFT.name)
                    }
                    CustomSegmentedView.Button.CENTRAL -> {
                        context?.showToast(CustomSegmentedView.Button.CENTRAL.name)
                    }
                    CustomSegmentedView.Button.RIGHT -> {
                        context?.showToast(CustomSegmentedView.Button.RIGHT.name)
                    }
                }
            }

            /*
            * ToggleView
            * */
            toggleView.onClickListener = { button ->
                hideKeyBoard()
                when (button) {
                    CustomToggleView.Button.LEFT -> {
                        context?.showToast(CustomToggleView.Button.LEFT.name)
                    }
                    CustomToggleView.Button.CENTRAL -> {
                        context?.showToast(CustomToggleView.Button.CENTRAL.name)
                    }
                    CustomToggleView.Button.RIGHT -> {
                        context?.showToast(CustomToggleView.Button.RIGHT.name)
                    }
                    else -> { }
                }
            }

            /*
            * ImagePicker
            * */
            imagePicker.apply {
                requiredFragmentManager = supportFragmentManager()
                onPickImageClickAction = { chooseImageFromGallery() }
                imagePicker.onRemoveImageClickAction = { removePhoto() }
            }

            /*
            * TagsView
            * */
            tagsView.apply {
                onSelectedTagsListener = { tags -> "Selected Tags=${tags.map { it.title }}" }
                dividerTextRes = R.string.add_new
                tagsView.tags = tagsMock
            }

            /*  AppCompatSeekBar + StepCounter */
            val stepNumber = 8
            stepCounter.stepNumbers = stepNumber
            appCompatSeekBar.apply {
                max = stepNumber - 1
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) { }
                    override fun onStartTrackingTouch(seekBar: SeekBar) { }
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        stepCounter.currentStep = seekBar.progress
                    }
                })
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

    private fun removePhoto() {
        // Remove from viewModel, Server etc
    }

    companion object {
        fun newInstance() = Tab3Fragment()
    }
}
