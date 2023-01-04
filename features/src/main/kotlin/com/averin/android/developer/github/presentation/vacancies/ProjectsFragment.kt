package com.averin.android.developer.github.presentation.vacancies

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.averin.android.developer.baseui.extension.android.content.debugClick
import com.averin.android.developer.baseui.extension.androidx.fragment.app.clearNavigationResult
import com.averin.android.developer.baseui.extension.androidx.fragment.app.getNavigationResult
import com.averin.android.developer.baseui.extension.androidx.fragment.app.onBackPressed
import com.averin.android.developer.baseui.extension.androidx.fragment.app.postDelayed
import com.averin.android.developer.baseui.extension.androidx.fragment.app.setKeyboardEventListener
import com.averin.android.developer.baseui.extension.androidx.lifecycle.observeSafe
import com.averin.android.developer.baseui.presentation.adapter.AdapterModel
import com.averin.android.developer.baseui.presentation.fragment.BaseErrorFragment
import com.averin.android.developer.dashboard.R
import com.averin.android.developer.dashboard.databinding.FrProjectsBinding
import com.averin.android.developer.github.domain.model.GitHubProject
import com.averin.android.developer.github.navigation.GitHubNavigation
import com.averin.android.developer.github.presentation.info.VacancyDashboardFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProjectsFragment : BaseErrorFragment(R.layout.fr_projects) {

    private val binding by viewBinding(FrProjectsBinding::bind)
    private val navigation: GitHubNavigation by inject()
    override val viewModel: ProjectsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { onBackPressed() }
        setKeyboardEventListener { isOpen -> }
        binding.projectsView.onVacancyClickListener = {
            onVacancySelected(it)
        }
        viewModel.run {
            vacanciesLiveData.observeSafe(viewLifecycleOwner) { bindVacancies(it) }
        }

        getNavigationResult<Int>(VacancyDashboardFragment.REQ_VACANCY_ACTIVATED_OR_CHANGED)?.let { createdVacancyId ->
            postDelayed(250) {
                context?.debugClick("$createdVacancyId is changed")
                clearNavigationResult<Int>(VacancyDashboardFragment.REQ_VACANCY_ACTIVATED_OR_CHANGED)
            }
        }
        loadVacancies()
    }

    private fun loadVacancies() {
        binding.projectsView.isLoading = viewModel.vacancies.isEmpty()
        viewModel.loadVacancies()
    }

    private fun bindVacancies(vacancies: List<GitHubProject>) {
        binding.projectsView.apply {
            isLoading = false
            items = vacancies
        }
    }

    private fun onVacancySelected(model: AdapterModel) {
        if (model is GitHubProject) {
            navigation.openProjectInfo(model.id)
        }
    }
}
