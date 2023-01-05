package com.averin.android.developer.github.presentation.info

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.base.util.RequestCodeGenerator
import com.averin.android.developer.baseui.extension.androidx.fragment.app.onBackPressed
import com.averin.android.developer.baseui.extension.androidx.lifecycle.observeSafe
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.baseui.presentation.fragment.BaseErrorFragment
import com.averin.android.developer.baseui.presentation.fragment.BaseFragment
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrGithubProjectInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProjectInfoFragment : BaseFragment(R.layout.fr_github_project_info) {

    override val viewModel: BaseViewModel? = null
    override val isFragmentWithNavigationPanel: Boolean = false

    private val binding by viewBinding(FrGithubProjectInfoBinding::bind)
    private val args: ProjectInfoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { onBackPressed() }
        binding.run {
            toolbar.onBackClickListener = { onBackPressed() }
            toolbar.title = args.project.name
            tvProjectInfo.text = args.project.toString()
        }
    }

    companion object {
        val REQ_VACANCY_ACTIVATED_OR_CHANGED = RequestCodeGenerator.next.toString()
    }
}
