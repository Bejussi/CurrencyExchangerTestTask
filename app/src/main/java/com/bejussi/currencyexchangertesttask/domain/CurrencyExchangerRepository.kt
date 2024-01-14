package com.bejussi.currencyexchangertesttask.domain

import com.bejussi.currencyexchangertesttask.data.remote.result.RatesResult
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.CurrencyResponce
import com.bejussi.currencyexchangertesttask.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangerRepository {
    fun getBalances(): Flow<List<Balance>>

    suspend fun getRates(): Flow<RatesResult<CurrencyResponce>>

    suspend fun addBalance(balance: Balance)

    fun getBalanceAmountByCurrencyCode(currencyCode: String): Flow<Double>

    fun getTransactionCount(): Flow<Int>

    suspend fun addTransaction(transaction: Transaction)
}