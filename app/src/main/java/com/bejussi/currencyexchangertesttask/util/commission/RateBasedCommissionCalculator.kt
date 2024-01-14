package com.bejussi.currencyexchangertesttask.util.commission

class RateBasedCommissionCalculator: CommissionCalculator {
    override fun calculateCommission(
        totalTransactions: Int,
        sellAmount: Double,
        fromCurrency: String
    ): Double {
        return when {
            sellAmount <= FREE_AMOUNT_LIMIT && fromCurrency == FREE_AMOUNT_CURRENCY -> FREE_COMMISSION
            else -> sellAmount * COMMISSION_RATE
        }
    }

    companion object {
        private const val FREE_COMMISSION = 0.0
        private const val FREE_AMOUNT_LIMIT = 200.0
        private const val FREE_AMOUNT_CURRENCY = "EUR"
        private const val COMMISSION_RATE = 0.007
    }
}