package com.bejussi.currencyexchangertesttask.di

import com.bejussi.currencyexchangertesttask.presentation.main.MainViewModel
import com.bejussi.currencyexchangertesttask.util.CommissionCalculator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    factory<CommissionCalculator> {
        CommissionCalculator()
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