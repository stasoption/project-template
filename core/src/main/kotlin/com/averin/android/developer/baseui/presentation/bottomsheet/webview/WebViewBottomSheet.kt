package com.averin.android.developer.baseui.presentation.bottomsheet.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.presentation.bottomsheet.BaseFullScreenBottomSheetFragment
import com.averin.android.developer.core.R
import com.averin.android.developer.core.databinding.FrWebviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebViewBottomSheet : BaseFullScreenBottomSheetFragment(R.layout.fr_webview) {

    private val viewModel: WebViewViewModel by viewModel()
    private val binding by viewBinding(FrWebviewBinding::bind)

    override val canBeHidden: Boolean = false
    override val canDraggable: Boolean = false
    override val backgroundDrawable: Int = R.drawable.bg_full_bottomsheet

    private val urlHttp: String? by lazy { arguments?.getString(ARG_URL) }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            toolbar.apply {
                onBackClickListener = { dismissAllowingStateLoss() }
            }

            webView.apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.databaseEnabled = true
                webChromeClient = WebChromeClient()
                webViewClient = CustomWebViewClient(requireActivity(), binding.loadingView)
                val headers: Map<String, String> = mutableMapOf<String, String>().apply {
                    if (viewModel.loginToken != null) {
                        put(AUTHORIZATION_HEADER, "$AUTHORIZATION_BEARER ${viewModel.loginToken}")
                    }
                }
                urlHttp?.let { loadUrl(it, headers) }
            }
        }
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val AUTHORIZATION_BEARER = "Bearer"
        private const val ARG_URL = "ARG_URL"
        fun newInstance(url: String) = WebViewBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_URL, url)
            }
        }
    }
}
