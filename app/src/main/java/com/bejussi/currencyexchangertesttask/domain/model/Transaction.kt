package com.bejussi.currencyexchangertesttask.domain.model

data class Transaction(
    val id: Int? = null,
    val toCurrency: String,
    val fromCurrency: String,
    val sellAmount: Double,
    val receiveAmount: Double,
    val commissionFee: Double
)
