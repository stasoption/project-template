package com.averin.android.developer.compose.presentation

import android.os.Bundle
import android.view.View
import com.averin.android.developer.baseui.extension.androidx.fragment.app.onBackPressed
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.dashboard.R

class ComposeFragment : BaseFragment(R.layout.fr_compose_placeholder) {

    override val viewModel: BaseViewModel? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { onBackPressed() }
    }
}
