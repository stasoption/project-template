package com.averin.android.developer.base.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun getRequestCode(reqCode: Int): Int {
    return reqCode and 0xFF
}

fun isGranted(result: IntArray): Boolean {
    for (item in result) {
        if (item != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

fun isStorageGranted(grantResults: IntArray): Boolean {
    return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
}

fun isGranted(context: Context, permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (!isGranted(context, permission)) {
            return false
        }
    }
    return true
}

fun isGranted(context: Context, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}
