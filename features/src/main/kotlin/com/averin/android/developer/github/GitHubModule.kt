package com.averin.android.developer.github

import com.averin.android.developer.github.data.GitHubApi
import com.averin.android.developer.github.data.GitHubRepositoryImpl
import com.averin.android.developer.github.domain.GitHubInteractor
import com.averin.android.developer.github.domain.GitHubRepository
import com.averin.android.developer.github.presentation.info.VacancyDashboardViewModel
import com.averin.android.developer.github.presentation.vacancies.ProjectsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val gitHubProjectsModule = module {
    single { get<Retrofit>().create(GitHubApi::class.java) }
    single<GitHubRepository> { GitHubRepositoryImpl(get()) }
    factory { GitHubInteractor(get()) }
    viewModel { VacancyDashboardViewModel(get()) }
    viewModel { ProjectsViewModel(get()) }
}
