package com.averin.android.developer.app

import com.averin.android.developer.app.init.AppVisibilityInitializer
import com.averin.android.developer.app.init.NotificationChannelInitializer
import com.averin.android.developer.app.init.StrictModeInitializer
import com.averin.android.developer.base.init.BaseAppInitializer
import com.averin.android.developer.base.json.adapter.MoshiAdapter
import com.squareup.moshi.Moshi
import org.koin.dsl.bind
import org.koin.dsl.module

@Suppress("USELESS_CAST")
val appModule = module {
    factory { StrictModeInitializer() } bind BaseAppInitializer::class
    factory { AppVisibilityInitializer(get()) } bind BaseAppInitializer::class
    factory { NotificationChannelInitializer(get()) } bind BaseAppInitializer::class
    single {
        Moshi.Builder()
            // add moshi adapters
            .apply {
                val adapters = getAll<MoshiAdapter>()
                adapters.forEach { adapter ->
                    add(adapter)
                }
                // You need to create adapters for all enums
                //  add(CurrencyCode::class.java, EnumJsonAdapter.create(CurrencyCode::class.java)
                //      .withUnknownFallback(CurrencyCode.USD))
            }
            .build()
    }
}
