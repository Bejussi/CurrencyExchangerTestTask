package com.bejussi.currencyexchangertesttask.presentation.main.model

sealed interface MainEvent {
    object SubmitTransaction: MainEvent
    data class SetSellAmount(val sellAmount: Double): MainEvent
    data class SetSellCurrency(val sellCurrency: String): MainEvent
    data class SetReceiveCurrency(val receiveCurrency: String): MainEvent
}