package com.bejussi.currencyexchangertesttask.data.remote

import com.bejussi.currencyexchangertesttask.data.remote.model.CurrencyResponceData
import retrofit2.http.GET

interface CurrencyExchangeRatesApi {
    @GET("/tasks/api/currency-exchange-rates")
    suspend fun getRates(): CurrencyResponceData
}