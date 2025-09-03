package com.example.effectivemobile

import android.app.Application
import com.example.effectivemobile.di.appModule
import com.example.effectivemobile.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                domainModule, // из модуля domain
                appModule     // из модуля app
            )
        }
    }
}