package com.averin.android.developer.app.main.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.R
import com.averin.android.developer.app.main.navigation.MainFlowFragment
import com.averin.android.developer.baseui.extension.android.view.doOnApplyWindowInsets
import com.averin.android.developer.databinding.AcMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.ac_main) {

    private val viewModel: MainViewModel by viewModel()
    private val binding by viewBinding(AcMainBinding::bind, R.id.mainView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInsets()

        val isAuthorized = if (intent.hasExtra(AUTHORIZED_EXTRA)) {
            intent.getBooleanExtra(AUTHORIZED_EXTRA, false)
        } else {
            viewModel.gitHubUserName != null
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.mainFragmentContainer, MainFlowFragment.newInstance(isAuthorized))
            }
        }
    }

    @Deprecated("Update to getInsets(int) with WindowInsetsCompat.Type.systemBars()")
    private fun setupInsets() {
        binding.mainView.doOnApplyWindowInsets { view, insets, _ ->
            val topInset = insets.systemWindowInsetTop
            val bottomInset = insets.systemWindowInsetBottom
            view.updatePadding(top = topInset, bottom = bottomInset)
            insets.consumeSystemWindowInsets()
        }
    }

    companion object {
        const val AUTHORIZED_EXTRA = "AUTHORIZED_EXTRA"
        fun makeIntent(context: Context, authorized: Boolean) = Intent(context, MainActivity::class.java).apply {
            putExtra(AUTHORIZED_EXTRA, authorized)
        }
    }
}
