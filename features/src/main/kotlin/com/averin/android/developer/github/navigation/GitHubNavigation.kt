package com.averin.android.developer.github.navigation

import com.averin.android.developer.github.domain.model.GitHubProject

interface GitHubNavigation {
    fun openProjectInfo(project: GitHubProject)
}
