package com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateReceiveAmount

interface CalculateReceivedAmountResult<T> {

    fun apply(calculateReceivedAmountActions: CalculateReceivedAmountActions)

    class Success<T>(private val data: T): CalculateReceivedAmountResult<T> {
        override fun apply(calculateReceivedAmountActions: CalculateReceivedAmountActions) {
            calculateReceivedAmountActions.updateSuccessReceiveAmount(receiveAmount = data as Double)
        }

    }

    class Error<T>(private val message: String): CalculateReceivedAmountResult<T> {
        override fun apply(calculateReceivedAmountActions: CalculateReceivedAmountActions) {
            calculateReceivedAmountActions.updateErrorReceiveAmount(message = message)
        }

    }
}