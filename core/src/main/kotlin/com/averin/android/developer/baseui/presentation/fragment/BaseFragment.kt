package com.averin.android.developer.baseui.presentation.fragment

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.averin.android.developer.base.extension.java.io.getFilePath
import com.averin.android.developer.base.util.FilePickerConst
import com.averin.android.developer.base.util.isGranted
import com.averin.android.developer.baseui.extension.android.app.pickFileByIntent
import com.averin.android.developer.baseui.extension.android.app.setNavigationPanelVisible
import com.averin.android.developer.baseui.extension.androidx.fragment.app.parentFragmentOnBackPressed
import com.averin.android.developer.baseui.presentation.BackPressable
import com.averin.android.developer.baseui.presentation.BaseViewModel
import timber.log.Timber

abstract class BaseFragment(contentLayoutId: Int = 0) : Fragment(contentLayoutId), BackPressable {

    abstract val viewModel: BaseViewModel?

    protected val galleryPermissionResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.entries.map { it.value }.all { it }) {
            requireActivity().pickFileByIntent("image/*", photoFromGalleryLauncher)
        }
    }

    protected var photoFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultPath = result.data?.data?.getFilePath(requireActivity())
        onImageSelected(resultPath)
    }

    private val fileSystemPermissionResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.entries.map { it.value }.all { it }) {
            requireActivity().pickFileByIntent("*/*", fileFromSystemLauncher)
        }
    }

    private var fileFromSystemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultPath = result.data?.data?.getFilePath(requireActivity())
        onFileSelected(resultPath)
    }

    open val isFragmentWithNavigationPanel: Boolean = true

    open fun onImageSelected(imagePath: String?) {
        Timber.d("You should override the method to use imagePath")
    }

    open fun onFileSelected(filePath: String?) {
        Timber.d("You should override the method to use filePath")
    }

    override fun onBackPressed() {
        parentFragmentOnBackPressed()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setNavigationPanelVisible(isFragmentWithNavigationPanel)
    }

    protected fun chooseImageFileFromSystem() {
        if (!isGranted(requireActivity(), FilePickerConst.PERMISSIONS_FILE_PICKER)) {
            fileSystemPermissionResult.launch(FilePickerConst.PERMISSIONS_FILE_PICKER)
        } else {
            requireActivity().pickFileByIntent("*/*", fileFromSystemLauncher)
        }
    }
}
