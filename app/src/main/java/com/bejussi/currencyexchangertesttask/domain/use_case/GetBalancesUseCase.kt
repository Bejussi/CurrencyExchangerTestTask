package com.bejussi.currencyexchangertesttask.domain.use_case

import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import kotlinx.coroutines.flow.Flow

class GetBalancesUseCase(private val currencyExchangerRepository: CurrencyExchangerRepository) {

    operator fun invoke(): Flow<List<Balance>> = currencyExchangerRepository.getBalances()
}