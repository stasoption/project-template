package com.averin.android.developer.github.data

import com.averin.android.developer.github.domain.model.GitHubProject
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("/users/{username}/repos")
    suspend fun getProjects(@Path("username") username: String): List<GitHubProject>?
}
