package com.averin.android.developer.github.presentation.projects

import com.averin.android.developer.baseui.extension.androidx.lifecycle.SingleLiveEvent
import com.averin.android.developer.baseui.presentation.BaseViewModel
import com.averin.android.developer.github.domain.GitHubInteractor
import com.averin.android.developer.github.domain.model.GitHubProject

class ProjectsViewModel(private val interactor: GitHubInteractor) : BaseViewModel() {

    val projectsLiveData = SingleLiveEvent<List<GitHubProject>>()
    var projects: List<GitHubProject> = arrayListOf()

    fun getProjects() = launchLoadingErrorJob {
        if (projects.isNotEmpty()) {
            projectsLiveData.value = projects
        }
        projects = interactor.getProjects().orEmpty()
        projectsLiveData.value = projects
    }
}
