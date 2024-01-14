package com.bejussi.currencyexchangertesttask.util.commission

class CommissionCalculatorStrategy {
    fun calculateCommission(
        totalTransactions: Int,
        sellAmount: Double,
        fromCurrency: String
    ): Double {
        val calculators = listOf(
            RateBasedCommissionCalculator(),
            TransactionCommissionCalculator()
        )
        return calculators.minOf {
            it.calculateCommission(
                totalTransactions,
                sellAmount,
                fromCurrency
            )
        }
    }

}