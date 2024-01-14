package com.bejussi.currencyexchangertesttask.presentation.main.actions.rates

import com.bejussi.currencyexchangertesttask.domain.model.Rates

interface RatesActions {
    fun updateRates(rates: Rates?, isError: Boolean)
}