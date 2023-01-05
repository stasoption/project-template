package com.averin.android.developer.github.domain

class GitHubInteractor(
    private val gitHubRepository: GitHubRepository
) {
    suspend fun getProjects() = gitHubRepository.getProjects()
}
