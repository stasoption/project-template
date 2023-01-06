package com.averin.android.developer.app.navigation.delegates

import com.averin.android.developer.dashboard.ProjectsGraphDirections
import com.averin.android.developer.github.domain.model.GitHubProject
import com.averin.android.developer.github.navigation.GitHubProjectNavControllerStorage
import com.averin.android.developer.github.navigation.GitHubNavigation

class GitHubProjectNavigationDelegate(
    private val navControllerStorage: GitHubProjectNavControllerStorage
) : GitHubNavigation {

    override fun openProjectInfo(project: GitHubProject) {
        navControllerStorage.navController
            ?.navigate(ProjectsGraphDirections.openProjectInfo(project))
    }
}
