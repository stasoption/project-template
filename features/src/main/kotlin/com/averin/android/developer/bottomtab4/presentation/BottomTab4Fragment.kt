package com.averin.android.developer.bottomtab4.presentation

import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.bottomtab4.navigation.BottomTab4Navigation
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrBottomTab4Binding
import org.koin.android.ext.android.inject

class BottomTab4Fragment : BaseFragment(R.layout.fr_bottom_tab_4) {
    private val binding by viewBinding(FrBottomTab4Binding::bind)
    private val navigation: BottomTab4Navigation by inject()
    override val viewModel: BaseViewModel? = null
}
