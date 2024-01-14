package com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateCommission

import com.bejussi.currencyexchangertesttask.domain.model.Transaction

interface CalculateCommissionAndSubmitTransactionResult<T> {

    fun apply(calculateCommissionAndSubmitTransactionActions: CalculateCommissionAndSubmitTransactionActions)

    class Success<T>(private val data: T) : CalculateCommissionAndSubmitTransactionResult<T> {
        override fun apply(calculateCommissionAndSubmitTransactionActions: CalculateCommissionAndSubmitTransactionActions) {
            calculateCommissionAndSubmitTransactionActions.showSuccessfulDialog(transaction = data as Transaction)
        }


    }

    class Error<T>(private val message: String) : CalculateCommissionAndSubmitTransactionResult<T> {
        override fun apply(calculateCommissionAndSubmitTransactionActions: CalculateCommissionAndSubmitTransactionActions) {
            calculateCommissionAndSubmitTransactionActions.showError(message = message)
        }

    }
}