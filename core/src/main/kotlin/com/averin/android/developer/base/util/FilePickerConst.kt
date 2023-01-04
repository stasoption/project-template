package com.averin.android.developer.base.util

import android.Manifest

object FilePickerConst {
    private val PERMISSIONS_CAMERA = arrayOf(
        Manifest.permission.CAMERA
    )
    val PERMISSIONS_FILE_PICKER = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
}
