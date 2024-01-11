package com.bejussi.currencyexchangertesttask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bejussi.currencyexchangertesttask.data.local.model.BalanceDto
import com.bejussi.currencyexchangertesttask.data.local.model.CurrencyDto
import com.bejussi.currencyexchangertesttask.data.local.model.TransactionDto

@Database(
    entities = [BalanceDto::class, TransactionDto::class, CurrencyDto::class],
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