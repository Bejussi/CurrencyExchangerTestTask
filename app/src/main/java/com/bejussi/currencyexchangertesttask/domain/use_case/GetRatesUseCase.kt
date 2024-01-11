package com.bejussi.currencyexchangertesttask.domain.use_case

import com.bejussi.currencyexchangertesttask.core.Resource
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.domain.model.CurrencyResponce
import kotlinx.coroutines.flow.Flow

class GetRatesUseCase(private val currencyExchangerRepository: CurrencyExchangerRepository) {

    suspend operator fun invoke(): Flow<Resource<CurrencyResponce>> =
        currencyExchangerRepository.getRates()
}