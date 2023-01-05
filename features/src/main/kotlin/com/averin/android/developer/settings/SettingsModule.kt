package com.averin.android.developer.settings

import com.averin.android.developer.settings.data.SettingsApi
import com.averin.android.developer.settings.data.SettingsRepositoryImpl
import com.averin.android.developer.settings.domain.SettingsInteractor
import com.averin.android.developer.settings.domain.SettingsRepository
import com.averin.android.developer.settings.presentation.MenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val settingsModule = module {
    single { get<Retrofit>().create(SettingsApi::class.java) }
    single<SettingsRepository> { SettingsRepositoryImpl(get(), get(), get()) }
    factory { SettingsInteractor(get()) }
    viewModel { MenuViewModel(get()) }
}
