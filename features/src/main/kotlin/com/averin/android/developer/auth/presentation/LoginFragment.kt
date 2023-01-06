package com.averin.android.developer.auth.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.auth.navigation.AuthNavigation
import com.averin.android.developer.baseui.extension.androidx.fragment.app.hideKeyBoard
import com.averin.android.developer.baseui.extension.androidx.lifecycle.observeSafe
import com.averin.android.developer.baseui.presentation.fragment.BaseErrorFragment
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrLoginBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseErrorFragment(R.layout.fr_login) {

    override val viewModel: LoginViewModel by viewModel()

    private val binding by viewBinding(FrLoginBinding::bind)
    private val navigation: AuthNavigation by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            etUserName.init { userName -> viewModel.email = userName }
            loginBtn.onClickListener = {
                etUserName.validate()
                if (etUserName.isValid) {
                    hideKeyBoard()
                    viewModel.loadProfile()
                }
            }

            tvLetMeIn.setOnClickListener {
                viewModel.loginWithMyPersonalUserName()
            }
        }

        viewModel.userResponseLiveData.observeSafe(viewLifecycleOwner) {
            navigation.openDashboard()
        }
        viewModel.loginWithoutLoginEvent.observe(viewLifecycleOwner) {
            navigation.openDashboard()
        }
    }
}
