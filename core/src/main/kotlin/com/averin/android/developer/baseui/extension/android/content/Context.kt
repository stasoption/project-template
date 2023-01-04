@file:Suppress("NOTHING_TO_INLINE")

package com.averin.android.developer.baseui.extension.android.content

import android.app.Activity
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Browser
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.averin.android.developer.core.R

const val AUTHORIZATION_HEADER = "Authorization"
const val AUTHORIZATION_BEARER = "Bearer"

private var toast: Toast? = null

fun Context.debugClick(message: String? = null) {
    showToast(message ?: "In progress..", Toast.LENGTH_SHORT)
}

fun Context.showToast(
    message: String,
    durationId: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.CENTER
) {
    toast?.cancel()
    toast = Toast.makeText(this, message, durationId).apply {
        setGravity(gravity, 0, 0)
    }
    toast?.show()
}

fun Context.openUrl(url: String, loginToken: String? = null) {
    if (!TextUtils.isEmpty(url)) {
        try {
            val headers: Bundle = Bundle().apply {
                putString(AUTHORIZATION_HEADER, "$AUTHORIZATION_BEARER $loginToken")
            }
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.intent.putExtra(Browser.EXTRA_HEADERS, headers)
            customTabsIntent.launchUrl(this, Uri.parse(url))
        } catch (e: ActivityNotFoundException) {
            // ignore
        }
    }
}

fun Context.copyToClipboard(label: String, text: String, onCopiedAction: (() -> Unit)? = null) {
    val clipboard: ClipboardManager? = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText(label, text)
    clipboard?.let {
        it.setPrimaryClip(clip)
        onCopiedAction?.invoke()
    }
}

fun Context.downloadFile(
    url: String,
    fileName: String,
    mimeType: String = "*/*",
    loginToken: String? = null
) {
    val request = DownloadManager.Request(Uri.parse(url)).apply {
        setTitle(fileName)
        setDescription("")
        setMimeType(mimeType)
        if (loginToken != null) {
            addRequestHeader("Authorization", loginToken)
        }
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        allowScanningByMediaScanner()
        setAllowedOverMetered(true)
        setAllowedOverRoaming(true)
    }
    val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
    downloadManager?.let {
        it.enqueue(request)
        this.showToast(this.getString(R.string.file_has_been_downloading))
    }
}

@ColorInt
fun Context.getColorPrimary(): Int {
    val typedValue = TypedValue()
    val a = obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimary))
    val color = a.getColor(0, 0)
    a.recycle()
    return color
}

fun Context.showKeyboard(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.sendEmail(subject: String?, message: String?, vararg recipients: String): Boolean {
    val email = Intent(Intent.ACTION_SENDTO)
    if (!TextUtils.isEmpty(subject)) {
        email.putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    if (!TextUtils.isEmpty(message)) {
        email.putExtra(Intent.EXTRA_TEXT, message)
    }
    if (recipients.isNotEmpty()) {
        email.putExtra(Intent.EXTRA_EMAIL, recipients)
    }
    // need this to prompts email client only
    email.type = "message/rfc822"
    email.data = Uri.parse("mailto:")
    val infos = packageManager.queryIntentActivities(email, 0)
    if (infos.size > 0) {
        startActivity(email)
        return true
    } else {
        return false
    }
}

fun Context.callToPhoneNumber(phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
    startActivity(intent)
}

inline fun Context.getColorKtx(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

inline fun Context.getDrawableKtx(@DrawableRes id: Int): Drawable? = AppCompatResources.getDrawable(this, id)

inline fun Context.getColorStateListKtx(@ColorRes id: Int): ColorStateList {
    return AppCompatResources.getColorStateList(this, id)
}

inline fun Context.getQuantityStringKtx(@PluralsRes id: Int, quantity: Int): String {
    return resources.getQuantityString(id, quantity, quantity)
}

inline fun Context.getQuantityStringKtx(@PluralsRes id: Int, quantity: Int, value: String): String {
    return resources.getQuantityString(id, quantity, value)
}

inline fun Context.getDimensionKtx(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}

inline fun Context.getDimensionPixelSizeKtx(@DimenRes id: Int): Int {
    return resources.getDimensionPixelSize(id)
}
