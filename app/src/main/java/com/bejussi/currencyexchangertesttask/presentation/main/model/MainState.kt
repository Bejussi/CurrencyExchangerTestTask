package com.bejussi.currencyexchangertesttask.presentation.main.model

data class MainState(
    val sellAmount: Double = 0.0,
    val sellCurrency: String = "EUR",
    val receiveCurrency: String = "AED",
    val receiveAmount: Double = 0.0,
    val commissionFee: Double = 0.0,
    val isSubmitAvailable: Boolean = false,
    val isInternetAvailable: Boolean = false
)
