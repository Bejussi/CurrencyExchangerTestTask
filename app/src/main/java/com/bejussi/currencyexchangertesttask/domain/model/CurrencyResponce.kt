package com.bejussi.currencyexchangertesttask.domain.model

data class CurrencyResponce(
    val base: String,
    val date: String,
    val rates: Rates
)
