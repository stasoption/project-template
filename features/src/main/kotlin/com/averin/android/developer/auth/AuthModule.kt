package com.averin.android.developer.auth

import com.averin.android.developer.auth.data.LoginRepositoryImpl
import com.averin.android.developer.auth.data.source.LoginApi
import com.averin.android.developer.auth.domain.LoginInteractor
import com.averin.android.developer.auth.domain.LoginRepository
import com.averin.android.developer.auth.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    single { get<Retrofit>().create(LoginApi::class.java) }
    single<LoginRepository> { LoginRepositoryImpl(get(), get(), get()) }
    factory { LoginInteractor(get()) }

    viewModel { LoginViewModel(get()) }
}
