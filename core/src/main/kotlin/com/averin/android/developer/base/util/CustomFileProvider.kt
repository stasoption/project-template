package com.averin.android.developer.base.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class CustomFileProvider : FileProvider() {
    companion object {
        fun getAuthority(context: Context): String {
            return "${context.packageName}.fileProvider"
        }
        fun getUriForFile(context: Context, file: File): Uri {
            return getUriForFile(context, "${context.packageName}.fileProvider", file)
        }
    }
}
