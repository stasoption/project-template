package com.averin.android.developer.baseui.extension.android.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.averin.android.developer.base.extension.java.io.isForbiddenPackage
import com.averin.android.developer.baseui.extension.android.content.getColorKtx
import com.averin.android.developer.baseui.presentation.NavigationPanel

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (imm != null && window != null) {
        var currentFocus = window.currentFocus
        if (currentFocus == null) {
            currentFocus = window.decorView.findFocus()
        }
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            currentFocus.clearFocus()
        }
    }
}

fun FragmentActivity.setNavigationPanelVisible(isVisible: Boolean) {
    this.supportFragmentManager.fragments.forEach { fragment ->
        if (fragment is NavigationPanel) {
            fragment.isNavigationPanelVisible(isVisible)
        }
        fragment.childFragmentManager.fragments.forEach { childFragment ->
            if (childFragment is NavigationPanel) {
                childFragment.isNavigationPanelVisible(isVisible)
            }
        }
    }
}

/**
 * Включает или отключает возможность поворорта экрана
 *
 * @param value
 * true - включить
 * false - отключить
 */
@SuppressLint("SourceLockedOrientationActivity")
fun Activity.lockRotation(value: Boolean) {
    requestedOrientation = if (value) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }
}

/**
 * Принудительно меняет ориентацию экрана
 */
@SuppressLint("SourceLockedOrientationActivity")
fun Activity.setOrientation(isPortrait: Boolean) {
    requestedOrientation = if (isPortrait) {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    } else {
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}

fun Activity.setStatusBarVisibility(isVisible: Boolean) {
    window?.run {
        if (isVisible) {
            clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarTextLightColor() {
    window?.decorView?.run {
        systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarTextDarkColor() {
    window?.decorView?.run {
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun Activity.setStatusBarBackgroundColor(@ColorInt color: Int) {
    window?.run {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}

fun Activity.setStatusBarBackgroundWhite() {
    setStatusBarBackgroundColor(getColorKtx(android.R.color.white))
}

fun Activity.setStatusBarBackgroundTransparent() {
    setStatusBarBackgroundColor(getColorKtx(android.R.color.transparent))
}

fun Activity.pickDocumentsByIntent(
    resultLauncher: ActivityResultLauncher<Intent>
) {
    val supportedMimeTypes = arrayOf(
        "application/pdf", // pdf
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // docx
    )
    val resultIntentList: MutableList<Intent> = ArrayList()
    val intent = Intent()
    intent.type = "*/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes)
    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

    val candidates = this.packageManager.queryIntentActivities(intent, 0)
    for (candidate in candidates) {
        val packageName = candidate.activityInfo.packageName
        if (isForbiddenPackage(packageName)) {
            continue
        }
        val iWantThis = Intent()
        iWantThis.type = "*/*"
        iWantThis.action = Intent.ACTION_OPEN_DOCUMENT
        iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        iWantThis.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        iWantThis.setPackage(packageName)
        resultIntentList.add(iWantThis)
    }
    if (resultIntentList.isEmpty()) {
        resultLauncher.launch(intent)
        return
    }
    val chooser = Intent.createChooser(resultIntentList.removeAt(0), "Select File(s)")
    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, resultIntentList.toTypedArray<Parcelable>())
    resultLauncher.launch(chooser)
}

@SuppressLint("QueryPermissionsNeeded")
fun Activity.pickFileByIntent(
    type: String,
    resultLauncher: ActivityResultLauncher<Intent>
) {
    val resultIntentList: MutableList<Intent> = ArrayList()
    val intent = Intent()
    intent.type = type
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

    val candidates = this.packageManager.queryIntentActivities(intent, 0)
    for (candidate in candidates) {
        val packageName = candidate.activityInfo.packageName
        if (isForbiddenPackage(packageName)) {
            continue
        }
        val iWantThis = Intent()
        iWantThis.type = type
        iWantThis.action = Intent.ACTION_OPEN_DOCUMENT
        iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        iWantThis.setPackage(packageName)
        resultIntentList.add(iWantThis)
    }
    if (resultIntentList.isEmpty()) {
        resultLauncher.launch(intent)
        return
    }
    val chooser = Intent.createChooser(resultIntentList.removeAt(0), "Select File(s)")
    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, resultIntentList.toTypedArray<Parcelable>())
    resultLauncher.launch(chooser)
}
