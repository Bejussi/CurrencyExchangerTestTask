package com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateCommission

import com.bejussi.currencyexchangertesttask.domain.model.Transaction

interface CalculateCommissionAndSubmitTransactionActions {

    fun showSuccessfulDialog(transaction: Transaction)

    fun showError(message: String)
}