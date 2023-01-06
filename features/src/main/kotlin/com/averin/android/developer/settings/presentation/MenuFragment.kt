package com.averin.android.developer.settings.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.auth.domain.model.UserResponse
import com.averin.android.developer.base.extension.kotlin.showFullScreenImageUrl
import com.averin.android.developer.base.util.toUIFormat
import com.averin.android.developer.baseui.error.GlobalErrorHandler
import com.averin.android.developer.baseui.extension.android.view.loadAvatar
import com.averin.android.developer.baseui.extension.android.view.textOrHide
import com.averin.android.developer.baseui.extension.androidx.fragment.app.onBackPressed
import com.averin.android.developer.baseui.extension.androidx.fragment.app.supportFragmentManager
import com.averin.android.developer.baseui.extension.androidx.lifecycle.observeSafe
import com.averin.android.developer.baseui.presentation.bottomsheet.DoubleActionBottomSheet
import com.averin.android.developer.baseui.presentation.fragment.BaseErrorFragment
import com.averin.android.developer.baseui.widget.CustomButton
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrMenuBinding
import com.averin.android.developer.settings.navigation.SettingsNavigation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : BaseErrorFragment(R.layout.fr_menu) {

    override val viewModel: MenuViewModel by viewModel()
    private val binding by viewBinding(FrMenuBinding::bind)
    private val navigation: SettingsNavigation by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { onBackPressed() }

        binding.run {
            btnLogout.onClickListener = { onLogoutClick() }
        }

        viewModel.run {
            getUserInfoEvent.observeSafe(viewLifecycleOwner) { obtainUserData(it) }
            logoutSuccessEvent.observe(viewLifecycleOwner) { isLogout ->
                loadingBinding.loadingView.isVisible = false
                logoutOrError(isLogout)
            }
        }

        initScreen()
    }

    private fun initScreen() {
        viewModel.getUserInfo()
    }

    private fun obtainUserData(userInfo: UserResponse) {
        loadingBinding.loadingView.isVisible = false
        binding.ivAvatar.loadAvatar(userInfo.avatarUrl)
        binding.ivAvatar.setOnClickListener {
            userInfo.avatarUrl.showFullScreenImageUrl(activity as AppCompatActivity)
        }

        binding.tvUserName.text = userInfo.name
        binding.tvUserEmail.textOrHide(userInfo.email)
        binding.tvUserLocation.textOrHide(userInfo.location)
        binding.tvUserCreated.text = getString(
            R.string.since_date,
            userInfo.createdAt.toUIFormat()
        )
    }

    private fun onLogoutClick() {
        DoubleActionBottomSheet.newInstance(
            title = getString(R.string.ask_for_logout),
            message = ""
        ).apply {
            bindFirstActionButton(
                text = R.string.no_action,
                type = CustomButton.Type.SECONDARY
            )
            bindSecondActionButton(
                text = R.string.yes_action,
                type = CustomButton.Type.WARNING
            ) {
                loadingBinding.loadingView.isVisible = true
                viewModel.logout()
            }
        }.show(supportFragmentManager(), DoubleActionBottomSheet::class.java.name)
    }

    private fun logoutOrError(isLogout: Boolean) {
        if (isLogout) {
            navigation.openLogin()
        } else {
            GlobalErrorHandler(errorView).apply {
                handleError(Exception(getString(R.string.unexpected_error)))
            }
        }
    }
}
