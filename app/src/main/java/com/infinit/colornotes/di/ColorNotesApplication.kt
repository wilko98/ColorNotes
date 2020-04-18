package com.infinit.colornotes.di

import android.app.Application
import com.infinit.colornotes.di.appModule
import org.koin.android.ext.android.startKoin
import org.koin.standalone.StandAloneContext.startKoin
import timber.log.Timber

class ColorNotesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.tag("ApplicationTag")
        startKoin(this,listOf(appModule))
    }
}