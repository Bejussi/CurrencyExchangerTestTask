package com.bejussi.currencyexchangertesttask.util.commission

interface CommissionCalculator {
    fun calculateCommission(
        totalTransactions: Int,
        sellAmount: Double,
        fromCurrency: String
    ): Double
}