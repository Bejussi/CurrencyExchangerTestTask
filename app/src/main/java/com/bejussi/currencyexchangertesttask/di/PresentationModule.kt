package com.bejussi.currencyexchangertesttask.di

import com.bejussi.currencyexchangertesttask.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<MainViewModel> {
        MainViewModel(
            getBalancesUseCase =  get(),
            getRatesUseCase = get()
        )
    }
}