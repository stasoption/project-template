package com.averin.android.developer.app.navigation.delegates

import com.averin.android.developer.dashboard.ProjectsGraphDirections
import com.averin.android.developer.github.navigation.GitHubNavControllerStorage
import com.averin.android.developer.github.navigation.GitHubNavigation

class GitHubNavigationDelegate(
    private val navControllerStorage: GitHubNavControllerStorage
) : GitHubNavigation {

    override fun openProjectInfo(id: Int) {
        navControllerStorage.navController
            ?.navigate(ProjectsGraphDirections.openProjectInfo(id))
    }
}
