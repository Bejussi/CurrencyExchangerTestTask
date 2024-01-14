package com.bejussi.currencyexchangertesttask.presentation.main.event

interface MainEvents {

    fun updateReceiveCurrencyCodeAndCalculateAmount(receiveCurrency: String)

    fun updateSellAmountAndCalculateAmount(sellAmount: Double)

    fun updateSellCurrencyCodeAndCalculateAmount(sellCurrency: String)

    fun submitTransaction()
}