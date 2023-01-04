package com.averin.android.developer.auth.presentation

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.auth.navigation.AuthNavigation
import com.averin.android.developer.baseui.extension.androidx.fragment.app.hideKeyBoard
import com.averin.android.developer.baseui.presentation.fragment.BaseErrorFragment
import com.averin.android.developer.baseui.widget.field.BaseFieldView
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrLoginBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseErrorFragment(R.layout.fr_login) {

    override val viewModel: LoginViewModel by viewModel()

    private val binding by viewBinding(FrLoginBinding::bind)
    private val navigation: AuthNavigation by inject()

    private val validateFields: Array<BaseFieldView> by lazy {
        arrayOf(binding.etLogin, binding.etPassword)
    }

    private val isFieldsEmpty: Boolean
        get() = viewModel.email.isNotEmpty() && viewModel.password.isNotEmpty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            etLogin.init { it.onEmailChanged() }
            etPassword.init { it.onPasswordChanged() }
            loginBtn.isEnabled = isFieldsEmpty
            loginBtn.onClickListener = {
                validateFields.forEach { it.validate() }
                if (validateFields.all { it.isValid }) {
                    hideKeyBoard()
                    viewModel.login()
                } else {
                    loginBtn.isEnabled = false
                }
            }
        }

        viewModel.loginSuccessEvent.observe(viewLifecycleOwner) { navigation.openDashboard() }
    }

    private fun String.onEmailChanged() {
        viewModel.email = this.trim()
        binding.loginBtn.isEnabled = isFieldsEmpty
        binding.etPassword.showError(null)
    }

    private fun String.onPasswordChanged() {
        viewModel.password = this.trim()
        binding.loginBtn.isEnabled = isFieldsEmpty
        binding.etLogin.showError(null)
    }
}
