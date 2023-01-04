package com.averin.android.developer.github.data

import com.averin.android.developer.github.domain.GitHubRepository

class GitHubRepositoryImpl(private val api: GitHubApi) : GitHubRepository {

    override suspend fun getVacancies() = api.getVacancies()
    override suspend fun getVacancy(id: Int) = api.getVacancy(id)
}
