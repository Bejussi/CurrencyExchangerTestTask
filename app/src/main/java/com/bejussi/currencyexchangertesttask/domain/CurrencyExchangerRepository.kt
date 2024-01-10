package com.bejussi.currencyexchangertesttask.domain

import com.bejussi.currencyexchangertesttask.core.Resource
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.CurrencyResponce
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangerRepository {
    fun getBalances(): Flow<List<Balance>>

    suspend fun getRates(base: String): Flow<Resource<CurrencyResponce>>
}