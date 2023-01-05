package com.averin.android.developer.github.data

import android.content.SharedPreferences
import com.averin.android.developer.base.util.FlowPreferences
import com.averin.android.developer.core.BuildConfig
import com.averin.android.developer.github.domain.GitHubRepository
import com.averin.android.developer.github.domain.model.GitHubProject
import com.squareup.moshi.Moshi

class GitHubRepositoryImpl(
    private val api: GitHubApi,
    private val moshi: Moshi,
    private val sharedPreferences: SharedPreferences
) : GitHubRepository {

    private val prefs = FlowPreferences(
        moshi = moshi,
        sharedPreferences = sharedPreferences,
        version = BuildConfig.PREFERENCES_VERSION,
        fallbackToDestructiveMigration = true
    )

    override var githubUserName: String? by prefs[FlowPreferences.ARG_GITHUB_USER_NAME, String::class]
    override suspend fun getProjects(): List<GitHubProject>? {
        return githubUserName?.let { api.getProjects(it) }
    }

}
