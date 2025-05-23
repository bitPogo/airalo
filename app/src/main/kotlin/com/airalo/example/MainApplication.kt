package com.airalo.example

import android.app.Application
import com.airalo.example.offer.di.resolveOfferKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger(Level.INFO)
            modules(
                resolveOfferKoin(),
            )
        }
    }
}
