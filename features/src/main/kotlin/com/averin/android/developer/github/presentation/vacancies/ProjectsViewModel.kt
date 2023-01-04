package com.averin.android.developer.github.presentation.vacancies

import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.github.domain.GitHubInteractor
import com.averin.android.developer.github.domain.model.GitHubProject

class ProjectsViewModel(private val interactor: GitHubInteractor) : BaseViewModel() {

    val vacanciesLiveData = SingleLiveEvent<List<GitHubProject>>()
    var vacancies: List<GitHubProject> = arrayListOf()

    fun loadVacancies() = launchLoadingErrorJob {
        if (vacancies.isNotEmpty()) {
            vacanciesLiveData.value = vacancies
        }
        vacancies = interactor.getVacancies().items
        vacanciesLiveData.value = vacancies
    }
}
