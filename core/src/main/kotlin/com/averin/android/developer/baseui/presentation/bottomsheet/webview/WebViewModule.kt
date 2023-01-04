package com.averin.android.developer.baseui.presentation.bottomsheet.webview

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val webViewModule = module {
    viewModel { WebViewViewModel(get(), get()) }
}
