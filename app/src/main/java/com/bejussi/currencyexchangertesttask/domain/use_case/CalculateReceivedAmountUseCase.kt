package com.bejussi.currencyexchangertesttask.domain.use_case

import com.bejussi.currencyexchangertesttask.core.roundValue
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateReceiveAmount.CalculateReceivedAmountResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CalculateReceivedAmountUseCase(
    private val currencyExchangerRepository: CurrencyExchangerRepository
) {

    operator fun invoke(
        sellAmount: Double,
        sellCurrency: String,
        receiveCurrency: String,
        rateValue: Double?
    ): Flow<CalculateReceivedAmountResult<Any>> = flow {
        try {
            if (sellAmount == 0.0) {
                emit(CalculateReceivedAmountResult.Error("Amount must be greater than 0"))
                return@flow
            }
            val sellCurrencyBalance =
                currencyExchangerRepository.getBalanceAmountByCurrencyCode(currencyCode = sellCurrency)
                    .first()
            if (sellAmount > sellCurrencyBalance) {
                emit(CalculateReceivedAmountResult.Error("Insufficient funds on balance"))
                return@flow
            }
            if (sellCurrency == receiveCurrency) {
                emit(CalculateReceivedAmountResult.Error("Exchange is impractical"))
                return@flow
            }
            if (rateValue == null) {
                emit(CalculateReceivedAmountResult.Error("Rate invalid, try again"))
                return@flow
            }
            val receivedAmount = (sellAmount * rateValue).roundValue()
            if (receivedAmount <= 0.0) {
                emit(CalculateReceivedAmountResult.Error("You will not profit from this exchange"))
                return@flow
            }
            emit(CalculateReceivedAmountResult.Success(data = receivedAmount))
        } catch (e: Exception) {
            emit(CalculateReceivedAmountResult.Error(message = "Try again"))
        }
    }
}