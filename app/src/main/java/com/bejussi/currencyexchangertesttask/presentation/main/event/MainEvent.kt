package com.bejussi.currencyexchangertesttask.presentation.main.event

interface MainEvent {

    fun apply(mainEvents: MainEvents)
    object SubmitTransaction: MainEvent {
        override fun apply(mainEvents: MainEvents) {
            mainEvents.submitTransaction()
        }
    }

    data class SetSellAmount(private val sellAmount: Double): MainEvent {
        override fun apply(mainEvents: MainEvents) {
            mainEvents.updateSellAmountAndCalculateAmount(sellAmount = sellAmount)
        }
    }

    data class SetSellCurrency(private val sellCurrency: String): MainEvent {
        override fun apply(mainEvents: MainEvents) {
            mainEvents.updateSellCurrencyCodeAndCalculateAmount(sellCurrency = sellCurrency)
        }
    }

    data class SetReceiveCurrency(private val receiveCurrency: String): MainEvent {
        override fun apply(mainEvents: MainEvents) {
            mainEvents.updateReceiveCurrencyCodeAndCalculateAmount(receiveCurrency = receiveCurrency)
        }
    }
}