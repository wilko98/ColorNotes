package com.infinit.colornotes.di

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.infinit.colornotes.BuildConfig
import com.infinit.colornotes.MainScreen.MainViewModel
import com.infinit.colornotes.db.NotesDao
import com.infinit.colornotes.db.NotesDatabase
import com.infinit.colornotes.utils.PrefManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { PrefManager(androidContext()) }
    single { createWebService<Api>(get("default"), BuildConfig.API_URL) }
    single("default") { createOkHttpClient(get()) }
    single { MainViewModel(get()) }
    single { createDatabase(androidApplication()) }
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

fun createDatabase(applicationContext: Application) = Room.databaseBuilder(
    applicationContext,
    NotesDatabase::class.java, "notes-database"
).fallbackToDestructiveMigration()
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