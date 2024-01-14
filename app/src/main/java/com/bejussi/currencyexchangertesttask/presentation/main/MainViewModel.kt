package com.bejussi.currencyexchangertesttask.presentation.main

import androidx.lifecycle.viewModelScope
import com.bejussi.currencyexchangertesttask.core.BaseViewModel
import com.bejussi.currencyexchangertesttask.core.getDoubleValueByName
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.Rates
import com.bejussi.currencyexchangertesttask.domain.model.Transaction
import com.bejussi.currencyexchangertesttask.domain.use_case.CalculateCommissionAndSubmitTransactionUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.CalculateReceivedAmountUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.GetBalancesUseCase
import com.bejussi.currencyexchangertesttask.domain.use_case.GetRatesUseCase
import com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateCommission.CalculateCommissionAndSubmitTransactionActions
import com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateReceiveAmount.CalculateReceivedAmountActions
import com.bejussi.currencyexchangertesttask.presentation.main.actions.rates.RatesActions
import com.bejussi.currencyexchangertesttask.presentation.main.event.MainEvent
import com.bejussi.currencyexchangertesttask.presentation.main.event.MainEvents
import com.bejussi.currencyexchangertesttask.presentation.main.model.MainState
import com.bejussi.currencyexchangertesttask.presentation.main.uiEvent.MainUiEventResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val getBalancesUseCase: GetBalancesUseCase,
    private val getRatesUseCase: GetRatesUseCase,
    private val calculateReceivedAmountUseCase: CalculateReceivedAmountUseCase,
    private val calculateCommissionAndSubmitTransactionUseCase: CalculateCommissionAndSubmitTransactionUseCase
) : BaseViewModel(), MainEvents, CalculateReceivedAmountActions,
    CalculateCommissionAndSubmitTransactionActions, RatesActions {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _balances: MutableStateFlow<List<Balance>> = MutableStateFlow(emptyList())
    val balances = _balances.asStateFlow()

    private val _rates: MutableStateFlow<Rates?> = MutableStateFlow(null)

    private var repeatJob: Job? = null

    init {
        getInitialData()
    }

    private fun getInitialData() {
        getBalances()
        repeatJob = getRates()
    }

    fun onEvent(event: MainEvent) = event.apply(this)

    private fun getBalances() {
        viewModelScope.launch {
            getBalancesUseCase().collect { balances ->
                _balances.value = balances
            }
        }
    }

    private fun getRates(): Job {
        return viewModelScope.launch {
            while (isActive) {
                getRatesUseCase().collect { ratesResult ->
                    ratesResult.apply(this@MainViewModel)
                }
                delay(DELAY)
            }
        }
    }

    private fun calculateReceivedAmount() {
        val rateValue = _rates.value?.getDoubleValueByName(_state.value.receiveCurrency)
        viewModelScope.launch {
            calculateReceivedAmountUseCase(
                sellAmount = _state.value.sellAmount,
                rateValue = rateValue,
                receiveCurrency = _state.value.receiveCurrency,
                sellCurrency = _state.value.sellCurrency
            ).collect { calculateReceivedAmountResult ->
                calculateReceivedAmountResult.apply(this@MainViewModel)
            }
        }
    }

    private fun sendTryAgainEvent(message: String?) {
        sendUiEvent(MainUiEventResult.ShowToast(message ?: "Try again"))
    }

    override fun updateReceiveCurrencyCodeAndCalculateAmount(receiveCurrency: String) {
        _state.value = state.value.copy(receiveCurrency = receiveCurrency)
        calculateReceivedAmount()
    }

    override fun updateSellAmountAndCalculateAmount(sellAmount: Double) {
        _state.value = state.value.copy(sellAmount = sellAmount)
        calculateReceivedAmount()
    }

    override fun updateSellCurrencyCodeAndCalculateAmount(sellCurrency: String) {
        _state.value = state.value.copy(sellCurrency = sellCurrency)
        calculateReceivedAmount()
    }

    override fun updateSuccessReceiveAmount(receiveAmount: Double) {
        _state.value = state.value.copy(
            receiveAmount = receiveAmount,
            isSubmitAvailable = true
        )
    }

    override fun updateErrorReceiveAmount(message: String) {
        sendTryAgainEvent(message)
        _state.value = state.value.copy(isSubmitAvailable = false)
    }

    override fun submitTransaction() {
        viewModelScope.launch {
            calculateCommissionAndSubmitTransactionUseCase(
                sellAmount = _state.value.sellAmount,
                sellCurrency = _state.value.sellCurrency,
                receiveCurrency = _state.value.receiveCurrency,
                receiveAmount = _state.value.receiveAmount
            ).collect { result ->
                result.apply(this@MainViewModel)
            }
        }
    }

    override fun showSuccessfulDialog(transaction: Transaction) {
        sendUiEvent(MainUiEventResult.ShowSuccessDialog(transaction = transaction))
    }

    override fun showError(message: String) {
        sendTryAgainEvent(message = message)
    }

    override fun updateRates(rates: Rates?, isError: Boolean) {
        _rates.value = rates
        _state.value = state.value.copy(
            isInternetAvailable = isError
        )
    }

    override fun onCleared() {
        repeatJob?.cancel()
        super.onCleared()
    }

    companion object {
        const val DELAY = 5000L
    }
}