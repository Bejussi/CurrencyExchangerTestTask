package com.bejussi.currencyexchangertesttask.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bejussi.currencyexchangertesttask.core.Resource
import com.bejussi.currencyexchangertesttask.core.UIEvent
import com.bejussi.currencyexchangertesttask.core.getDoubleValueByName
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.Rates
import com.bejussi.currencyexchangertesttask.domain.use_case.GetBalancesUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.GetRatesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Math.round

class MainViewModel(
    private val getBalancesUseCase: GetBalancesUseCase,
    private val getRatesUseCase: GetRatesUseCase
) : ViewModel() {

    private val _balances: MutableStateFlow<List<Balance>> = MutableStateFlow(emptyList())
    val balances = _balances.asStateFlow()

    private val _rates: MutableStateFlow<Rates?> = MutableStateFlow(null)

    private val _receiveAmount: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val receiveAmount = _receiveAmount.asStateFlow()

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getBalances()
        getRates()
    }

    suspend fun calculateConvertation(
        sellAmount: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val fromCurrencyBalance =
            balances.value.find { it.currencyCode == fromCurrency }?.balance
                ?: 0.0

        if (sellAmount.toDouble() > fromCurrencyBalance) {
            _receiveAmount.value = 0.0
            sendUiEvent("Insufficient funds on balance")
        } else {
            val rateValue = _rates.value?.getDoubleValueByName(toCurrency)
            if (rateValue != null) {
                val convertedCurrency = round(sellAmount.toDouble() * rateValue)

                val commissionFee = CommissionCalculator().calculateCommission(
                    totalTransactions = 6,
                    transactionAmount = sellAmount.toDouble(),
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    balances = balances.value
                )

                val convertedCurrencyWithCommissionFee = convertedCurrency - commissionFee
                _receiveAmount.value = convertedCurrencyWithCommissionFee
            }
        }
    }

    private fun getRates() {
        viewModelScope.launch {
            while (true) {
                getRatesUseCase("").collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            sendUiEvent(resource.message ?: "Unknowm error")
                        }

                        is Resource.Success -> {
                            _rates.value = resource.data?.rates
                        }
                    }
                }
                delay(DELAY)
            }
        }
    }

    private suspend fun sendUiEvent(message: String) {
        _eventFlow.send(
            UIEvent.ShowToast(message)
        )
    }

    private fun getBalances() {
        viewModelScope.launch {
            getBalancesUseCase().collect { balances ->
                _balances.value = balances
            }
        }
    }

    companion object {
        const val DELAY = 5000L
    }
}