package com.bejussi.currencyexchangertesttask.domain.use_case

import com.bejussi.currencyexchangertesttask.core.Resource
import com.bejussi.currencyexchangertesttask.core.roundValue
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
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
    ): Flow<Resource<Any>> = flow {
        try {
            if (sellAmount == 0.0) {
                emit(Resource.Error("Amount must be greater than 0"))
                return@flow
            }
            val sellCurrencyBalance = currencyExchangerRepository.getBalanceAmountByCurrencyCode(currencyCode = sellCurrency).first()
            if (sellAmount > sellCurrencyBalance) {
                emit(Resource.Error("Insufficient funds on balance"))
                return@flow
            }
            if (sellCurrency == receiveCurrency) {
                emit(Resource.Error("Exchange is impractical"))
                return@flow
            }
            if (rateValue == null) {
                emit(Resource.Error("Rate invalid, try again"))
                return@flow
            }
            val receivedAmount = (sellAmount * rateValue).roundValue()
            if (receivedAmount <= 0.0) {
                emit(Resource.Error("You will not profit from this exchange"))
                return@flow
            }
            emit(Resource.Success(data = receivedAmount))
        } catch (e: Exception) {
            emit(Resource.Error(message = "Try again"))
        }
    }
}