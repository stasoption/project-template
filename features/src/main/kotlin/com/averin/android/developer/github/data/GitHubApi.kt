package com.averin.android.developer.github.data

import com.averin.android.developer.github.domain.model.GetVacancyResponse
import com.averin.android.developer.github.domain.model.GitHubProject
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("vacancy")
    suspend fun getVacancies(): GetVacancyResponse

    @GET("vacancy/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: Int): GitHubProject
}
