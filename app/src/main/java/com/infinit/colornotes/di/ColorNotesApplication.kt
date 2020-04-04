package com.infinit.colornotes.di

import android.app.Application
import com.infinit.colornotes.di.appModule
import org.koin.android.ext.android.startKoin
import org.koin.standalone.StandAloneContext.startKoin

class ColorNotesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this,listOf(appModule))
    }
}