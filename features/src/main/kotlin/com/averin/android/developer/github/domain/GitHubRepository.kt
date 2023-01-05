package com.averin.android.developer.github.domain

import com.averin.android.developer.github.domain.model.GitHubProject

interface GitHubRepository {
    var githubUserName: String?
    suspend fun getProjects(): List<GitHubProject>?
}
