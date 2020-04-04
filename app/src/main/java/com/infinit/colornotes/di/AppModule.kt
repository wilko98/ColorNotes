package com.infinit.colornotes.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.infinit.colornotes.BuildConfig
import com.infinit.colornotes.ui.main.MainViewModel
import com.infinit.colornotes.utils.PrefManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { PrefManager(androidContext()) }
    factory { createWebService<Api>(get("default"), BuildConfig.API_URL) }
    factory("default") { createOkHttpClient(get()) }
    factory {
        MainViewModel(get())
    }
}

const val timeout = 60L

val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}


fun createOkHttpClient(prefManager: PrefManager) =
    OkHttpClient.Builder()
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(TokenInterceptor(prefManager))
        .build()

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    return retrofit.create(T::class.java)
}