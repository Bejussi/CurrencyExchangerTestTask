package com.bejussi.currencyexchangertesttask.di

import com.bejussi.currencyexchangertesttask.domain.use_case.CalculateCommissionAndSubmitTransactionUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.CalculateReceivedAmountUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.GetBalancesUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.GetRatesUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetBalancesUseCase> {
        GetBalancesUseCase(
            currencyExchangerRepository = get()
        )
    }

    factory<GetRatesUseCase> {
        GetRatesUseCase(
            currencyExchangerRepository = get()
        )
    }

    factory<CalculateReceivedAmountUseCase> {
        CalculateReceivedAmountUseCase(
            currencyExchangerRepository = get()
        )
    }

    factory<CalculateCommissionAndSubmitTransactionUseCase> {
        CalculateCommissionAndSubmitTransactionUseCase(
            currencyExchangerRepository = get()
        )
    }
}