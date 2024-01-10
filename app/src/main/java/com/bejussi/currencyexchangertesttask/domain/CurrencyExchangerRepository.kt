package com.bejussi.currencyexchangertesttask.domain

import com.bejussi.currencyexchangertesttask.domain.model.Balance
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangerRepository {
    fun getBalances(): Flow<List<Balance>>
}