package com.bejussi.currencyexchangertesttask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bejussi.currencyexchangertesttask.data.local.model.BalanceDto

@Database(
    entities = [BalanceDto::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyExchangerDatabase: RoomDatabase() {
    abstract fun balanceDao(): BalanceDao

    companion object {
        const val DATABASE_NAME = "currency_exchanger_database"

        val PREPOPULATE_DATA = BalanceDto(currencyCode = "EUR", balance = 1000.00)
    }
}