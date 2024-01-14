package com.bejussi.currencyexchangertesttask.util.commission

class TransactionCommissionCalculator: CommissionCalculator {
    override fun calculateCommission(
        totalTransactions: Int,
        sellAmount: Double,
        fromCurrency: String
    ): Double {
        return when {
            totalTransactions + 1 <= FREE_TRANSACTION_LIMIT -> FREE_COMMISSION
            totalTransactions % FREE_TENTH_TRANSACTION == 0 -> FREE_COMMISSION
            else -> sellAmount * COMMISSION_RATE
        }
    }

    companion object {
        private const val FREE_COMMISSION = 0.0
        private const val FREE_TRANSACTION_LIMIT = 5
        private const val FREE_TENTH_TRANSACTION = 10
        private const val COMMISSION_RATE = 0.007
    }
}