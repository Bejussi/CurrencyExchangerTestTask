package com.bejussi.currencyexchangertesttask.data.remote.model

import com.google.gson.annotations.SerializedName

data class CurrencyResponceData(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val ratesData: RatesData
)