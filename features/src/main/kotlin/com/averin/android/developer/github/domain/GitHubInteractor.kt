package com.averin.android.developer.github.domain

class GitHubInteractor(
    private val gitHubRepository: GitHubRepository
) {
    suspend fun getVacancies() = gitHubRepository.getVacancies()
    suspend fun getVacancy(id: Int) = gitHubRepository.getVacancy(id)
}
