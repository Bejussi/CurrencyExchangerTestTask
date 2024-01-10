package com.bejussi.currencyexchangertesttask

import android.app.Application
import com.bejussi.currencyexchangertesttask.di.dataModule
import com.bejussi.currencyexchangertesttask.di.domainModule
import com.bejussi.currencyexchangertesttask.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CurrencyExchangerApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CurrencyExchangerApplication)
            modules(
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}