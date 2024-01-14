package com.bejussi.currencyexchangertesttask.data.remote.result

import com.bejussi.currencyexchangertesttask.domain.model.Rates
import com.bejussi.currencyexchangertesttask.presentation.main.actions.rates.RatesActions

interface RatesResult<T> {

    fun apply(ratesActions: RatesActions)

    class Success<T>(private val rates: Rates?, private val isError: Boolean) : RatesResult<T> {
        override fun apply(ratesActions: RatesActions) {
            ratesActions.updateRates(rates = rates, isError = isError)
        }

    }
}