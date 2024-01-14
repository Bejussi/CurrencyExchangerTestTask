package com.bejussi.currencyexchangertesttask.domain.use_case

import com.bejussi.currencyexchangertesttask.core.roundValue
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.Transaction
import com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateCommission.CalculateCommissionAndSubmitTransactionResult
import com.bejussi.currencyexchangertesttask.util.commission.CommissionCalculatorStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CalculateCommissionAndSubmitTransactionUseCase(
    private val currencyExchangerRepository: CurrencyExchangerRepository
) {
    operator fun invoke(
        sellAmount: Double,
        sellCurrency: String,
        receiveCurrency: String,
        receiveAmount: Double
    ): Flow<CalculateCommissionAndSubmitTransactionResult<Any>> = flow {
        try {
            val totalTransactionCount = currencyExchangerRepository.getTransactionCount().first()
            val sellCurrencyBalance =
                currencyExchangerRepository.getBalanceAmountByCurrencyCode(currencyCode = sellCurrency)
                    .first()
            val receiveCurrencyBalance =
                currencyExchangerRepository.getBalanceAmountByCurrencyCode(currencyCode = receiveCurrency)
                    .first()

            val calculatedCommission = CommissionCalculatorStrategy().calculateCommission(
                totalTransactions = totalTransactionCount,
                fromCurrency = sellCurrency,
                sellAmount = sellAmount
            ).roundValue()

            val balanceAfterTransaction = sellCurrencyBalance - (sellAmount + calculatedCommission)

            if (balanceAfterTransaction < 0) {
                emit(CalculateCommissionAndSubmitTransactionResult.Error(message = "Insufficient funds to pay the fee"))
                return@flow
            }

            val sellBalanceUpdate = Balance(
                currencyCode = sellCurrency,
                balance = balanceAfterTransaction.roundValue()
            )

            val receivedBalanceAmount = receiveCurrencyBalance + receiveAmount

            val receiveBalanceUpdate = Balance(
                currencyCode = receiveCurrency,
                balance = receivedBalanceAmount.roundValue()
            )

            currencyExchangerRepository.addBalance(sellBalanceUpdate)
            currencyExchangerRepository.addBalance(receiveBalanceUpdate)

            val transaction = Transaction(
                id = null,
                toCurrency = receiveCurrency,
                fromCurrency = sellCurrency,
                sellAmount = sellAmount,
                receiveAmount = receiveAmount,
                commissionFee = calculatedCommission

            )
            currencyExchangerRepository.addTransaction(transaction = transaction)

            emit(CalculateCommissionAndSubmitTransactionResult.Success(data = transaction))
        } catch (e: Exception) {
            emit(CalculateCommissionAndSubmitTransactionResult.Error(message = "Try again"))
        }
    }
}