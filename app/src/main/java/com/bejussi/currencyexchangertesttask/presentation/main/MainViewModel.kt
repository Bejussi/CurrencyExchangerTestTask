package com.bejussi.currencyexchangertesttask.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.use_case.GetBalancesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel (
    private val getBalancesUseCase: GetBalancesUseCase
): ViewModel() {

    private val _balances: MutableStateFlow<List<Balance>> = MutableStateFlow(emptyList())
    val balances = _balances.asStateFlow()

    init {
        getBalances()
    }

    private fun getBalances() {
        viewModelScope.launch {
            getBalancesUseCase().collect { balances ->
                _balances.value = balances
            }
        }
    }
}