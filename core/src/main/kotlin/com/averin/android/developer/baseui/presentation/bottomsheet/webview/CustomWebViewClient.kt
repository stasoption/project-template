package com.averin.android.developer.baseui.presentation.bottomsheet.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.averin.android.developer.baseui.presentation.bottomsheet.DoubleActionBottomSheet
import com.averin.android.developer.baseui.widget.CustomButton
import com.averin.android.developer.core.R

class CustomWebViewClient(val context: Context, val loadingView: View? = null) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        loadingView?.isVisible = true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        loadingView?.isVisible = false
    }

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError) {
        val message: String = when (error.primaryError) {
            SslError.SSL_UNTRUSTED -> context.getString(R.string.ssl_untrusted)
            SslError.SSL_EXPIRED -> context.getString(R.string.ssl_expired)
            SslError.SSL_IDMISMATCH -> context.getString(R.string.ssl_idmismatch)
            SslError.SSL_NOTYETVALID -> context.getString(R.string.ssl_notyetvalid)
            else -> context.getString(R.string.certificate_error)
        }
        DoubleActionBottomSheet.newInstance(message).apply {
            bindFirstActionButton(
                text = R.string.continue_action,
                type = CustomButton.Type.PRIMARY
            ) { handler.proceed() }
            bindSecondActionButton(
                text = R.string.cancell,
                type = CustomButton.Type.SECONDARY
            ) { handler.cancel() }
        }.show((context as AppCompatActivity).supportFragmentManager, DoubleActionBottomSheet::class.java.name)
    }
}
