package com.averin.android.developer.app.network.rest

import com.averin.android.developer.BuildConfig
import com.averin.android.developer.base.util.SSLNetworkUtils
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Collections
import java.util.concurrent.TimeUnit

val restModule = module {

    single { AuthorizationInterceptor(get(), get()) }
    single { DebugLoggingInterceptor() }

    single {
        val builder = OkHttpClient.Builder()

        // logs
        if (BuildConfig.DEBUG) {
            SSLNetworkUtils.disableSSLValidation(builder)
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        builder.apply {
            protocols(Collections.singletonList(Protocol.HTTP_1_1))
            addInterceptor(get<AuthorizationInterceptor>())
            retryOnConnectionFailure(true)
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }

        builder.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }
}
