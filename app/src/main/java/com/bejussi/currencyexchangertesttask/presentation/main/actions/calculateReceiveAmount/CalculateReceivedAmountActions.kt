package com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateReceiveAmount

interface CalculateReceivedAmountActions {

    fun updateSuccessReceiveAmount(receiveAmount: Double)

    fun updateErrorReceiveAmount(message: String)
}