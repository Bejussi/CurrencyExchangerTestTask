package com.bejussi.currencyexchangertesttask.presentation.main

import com.bejussi.currencyexchangertesttask.domain.model.Balance

class CommissionCalculator {
    fun calculateCommission(
        totalTransactions: Int,
        transactionAmount: Double,
        fromCurrency: String,
        toCurrency: String,
        balances: List<Balance>
    ): Double {
        val fromBalance = balances.find { it.currencyCode == fromCurrency }?.balance ?: 0.0
        val toBalance = balances.find { it.currencyCode == toCurrency }?.balance ?: 0.0

        return when {
            totalTransactions <= FREE_TRANSACTION_LIMIT -> 0.0
            totalTransactions % FREE_TENTH_TRANSACTION == 0 -> 0.0
            transactionAmount <= FREE_AMOUNT_LIMIT && fromCurrency == FREE_AMOUNT_CURRENCY -> 0.0
            else -> transactionAmount * COMMISSION_RATE
        }
    }

    companion object {
        private const val FREE_TRANSACTION_LIMIT = 5
        private const val FREE_TENTH_TRANSACTION = 10
        private const val FREE_AMOUNT_LIMIT = 200.0
        private const val FREE_AMOUNT_CURRENCY = "EUR"
        private const val COMMISSION_RATE = 0.007
    }

}