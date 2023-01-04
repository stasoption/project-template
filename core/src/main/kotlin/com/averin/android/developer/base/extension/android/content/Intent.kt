package com.averin.android.developer.base.extension.android.content

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.averin.android.developer.base.extension.java.io.fromSharedContent
import java.io.File

fun Intent.getSharedContent(context: Context): List<File> {
    val sharedFiles: MutableList<File> = mutableListOf()
    if (this.action != Intent.ACTION_SEND_MULTIPLE) {
        val uri = (getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)
        uri?.fromSharedContent(context)?.let { file -> sharedFiles.add(file) }
    } else {
        val list = getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)
        list.orEmpty().forEach { data ->
            (data as? Uri)?.fromSharedContent(context)?.let { file ->
                sharedFiles.add(file)
            }
        }
    }
    return sharedFiles
}
