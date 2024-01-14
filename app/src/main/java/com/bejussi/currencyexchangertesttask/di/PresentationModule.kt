package com.bejussi.currencyexchangertesttask.di

import com.bejussi.currencyexchangertesttask.presentation.main.MainViewModel
import com.bejussi.currencyexchangertesttask.util.commission.CommissionCalculatorStrategy
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    factory<CommissionCalculatorStrategy> {
        CommissionCalculatorStrategy()
    }

    viewModel<MainViewModel> {
        MainViewModel(
            getBalancesUseCase = get(),
            getRatesUseCase = get(),
            calculateReceivedAmountUseCase = get(),
            calculateCommissionAndSubmitTransactionUseCase = get()
        )
    }
}