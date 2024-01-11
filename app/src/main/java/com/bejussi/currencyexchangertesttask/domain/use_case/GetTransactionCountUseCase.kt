package com.bejussi.currencyexchangertesttask.domain.use_case

import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionCountUseCase(private val currencyExchangerRepository: CurrencyExchangerRepository) {
    operator fun invoke(): Flow<Int> = currencyExchangerRepository.getTransactionCount()
}