package com.bejussi.currencyexchangertesttask.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bejussi.currencyexchangertesttask.core.Resource
import com.bejussi.currencyexchangertesttask.core.UIEvent
import com.bejussi.currencyexchangertesttask.core.getDoubleValueByName
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.Rates
import com.bejussi.currencyexchangertesttask.domain.model.Transaction
import com.bejussi.currencyexchangertesttask.domain.use_case.CalculateCommissionAndSubmitTransactionUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.CalculateReceivedAmountUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.GetBalancesUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.GetRatesUseCase
import com.bejussi.currencyexchangertesttask.presentation.main.model.MainEvent
import com.bejussi.currencyexchangertesttask.presentation.main.model.MainState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val getBalancesUseCase: GetBalancesUseCase,
    private val getRatesUseCase: GetRatesUseCase,
    private val calculateReceivedAmountUseCase: CalculateReceivedAmountUseCase,
    private val calculateCommissionAndSubmitTransactionUseCase: CalculateCommissionAndSubmitTransactionUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private val _balances: MutableStateFlow<List<Balance>> = MutableStateFlow(emptyList())
    val balances = _balances.asStateFlow()

    private val _rates: MutableStateFlow<Rates?> = MutableStateFlow(null)

    var repeatJob: Job? = null

    init {
        getInitialData()
    }

    private fun getInitialData() {
        getBalances()
        repeatJob = getRates()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.SetReceiveCurrency -> {
                _state.value = state.value.copy(
                    receiveCurrency = event.receiveCurrency
                )
                calculateReceivedAmount()
            }

            is MainEvent.SetSellAmount -> {
                _state.value = state.value.copy(
                    sellAmount = event.sellAmount
                )
                calculateReceivedAmount()
            }

            is MainEvent.SetSellCurrency -> {
                _state.value = state.value.copy(
                    sellCurrency = event.sellCurrency
                )
                calculateReceivedAmount()
            }

            MainEvent.SubmitTransaction -> submitTransaction()
        }
    }

    private fun calculateReceivedAmount() {
        viewModelScope.launch {
            val rateValue = _rates.value?.getDoubleValueByName(_state.value.receiveCurrency)
            calculateReceivedAmountUseCase(
                sellAmount = _state.value.sellAmount,
                rateValue = rateValue,
                receiveCurrency = _state.value.receiveCurrency,
                sellCurrency = _state.value.sellCurrency
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            receiveAmount = resource.data as Double,
                            isSubmitAvailable = true
                        )
                    }

                    else -> {
                        sendUiEvent(
                            UIEvent.ShowToast(
                                resource.message ?: "Try again"
                            )
                        )
                        _state.value = state.value.copy(
                            isSubmitAvailable = false
                        )
                    }
                }
            }
        }
    }

    private fun submitTransaction() {
        viewModelScope.launch {
            calculateCommissionAndSubmitTransactionUseCase(
                sellAmount = _state.value.sellAmount,
                sellCurrency = _state.value.sellCurrency,
                receiveCurrency = _state.value.receiveCurrency,
                receiveAmount = _state.value.receiveAmount

            ).collect { resource ->
                when (resource) {

                    is Resource.Success -> {
                        sendUiEvent(UIEvent.ShowSuccessDialog(resource.data as Transaction))
                    }

                    else -> sendUiEvent(
                        UIEvent.ShowToast(
                            resource.message ?: "Try again"
                        )
                    )
                }
            }
        }
    }

    private fun getRates(): Job {
        return viewModelScope.launch {
            while (isActive) {
                getRatesUseCase().collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            sendUiEvent(
                                UIEvent.ShowToast(
                                    resource.message ?: "Try again"
                                )
                            )
                        }

                        is Resource.Success -> {
                            _rates.value = resource.data?.rates
                            _state.value = state.value.copy(
                                isInternetAvailable = true
                            )
                        }

                        is Resource.InternetError -> {
                            _state.value = state.value.copy(
                                isInternetAvailable = false
                            )
                        }
                    }
                }
                delay(DELAY)
            }
        }
    }

    private fun getBalances() {
        viewModelScope.launch {
            getBalancesUseCase().collect { balances ->
                _balances.value = balances
            }
        }
    }

    private fun sendUiEvent(uiEvent: UIEvent) {
        viewModelScope.launch {
            _eventFlow.send(uiEvent)
        }
    }

    override fun onCleared() {
        repeatJob?.cancel()
        super.onCleared()
    }

    companion object {
        const val DELAY = 5000L
    }
}