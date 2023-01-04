package com.averin.android.developer.github.domain

import com.averin.android.developer.github.domain.model.GetVacancyResponse
import com.averin.android.developer.github.domain.model.GitHubProject

interface GitHubRepository {
    suspend fun getVacancies(): GetVacancyResponse
    suspend fun getVacancy(id: Int): GitHubProject
}
