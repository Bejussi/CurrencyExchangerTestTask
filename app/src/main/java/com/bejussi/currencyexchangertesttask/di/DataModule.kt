package com.bejussi.currencyexchangertesttask.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bejussi.currencyexchangertesttask.data.CurrencyExchangerRepositoryImpl
import com.bejussi.currencyexchangertesttask.data.local.BalanceDao
import com.bejussi.currencyexchangertesttask.data.local.CurrencyExchangerDatabase
import com.bejussi.currencyexchangertesttask.data.remote.CurrencyExchangeRatesApi
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<CurrencyExchangerDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            CurrencyExchangerDatabase::class.java,
            CurrencyExchangerDatabase.DATABASE_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        get<CurrencyExchangerDatabase>().balanceDao()
                            .addBalance(CurrencyExchangerDatabase.PREPOPULATE_DATA)
                    }
                }
            })
            .build()
    }

    single<BalanceDao> {
        val database = get<CurrencyExchangerDatabase>()
        database.balanceDao()
    }

    single<CurrencyExchangerRepository> {
        CurrencyExchangerRepositoryImpl(
            balanceDao = get(),
            currencyExchangeRatesApi = get()
        )
    }

    single<GsonConverterFactory> {
        GsonConverterFactory.create()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://developers.paysera.com/")
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    single<CurrencyExchangeRatesApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(CurrencyExchangeRatesApi::class.java)
    }
}