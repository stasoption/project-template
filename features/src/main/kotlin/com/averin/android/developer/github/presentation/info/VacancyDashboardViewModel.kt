package com.averin.android.developer.github.presentation.info

import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.github.domain.GitHubInteractor
import com.averin.android.developer.github.domain.model.GitHubProject

class VacancyDashboardViewModel(private val interactor: GitHubInteractor) : BaseViewModel() {

    val vacancyLiveData = SingleLiveEvent<GitHubProject>()

    fun getVacancy(id: Int) = launchLoadingErrorJob {
        vacancyLiveData.value = interactor.getVacancy(id)
    }
}
