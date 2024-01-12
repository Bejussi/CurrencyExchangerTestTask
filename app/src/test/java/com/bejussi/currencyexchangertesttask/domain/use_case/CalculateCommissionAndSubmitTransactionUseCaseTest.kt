package com.bejussi.currencyexchangertesttask.domain.use_case

import com.bejussi.currencyexchangertesttask.core.Resource
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.domain.model.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CalculateCommissionAndSubmitTransactionUseCaseTest {

    private val currencyExchangerRepository = mock<CurrencyExchangerRepository>()

    private lateinit var calculateCommissionAndSubmitTransactionUseCase: CalculateCommissionAndSubmitTransactionUseCase

    @After
    fun afterEach() {
        Mockito.reset(currencyExchangerRepository)
    }

    @Before
    fun beforeEach() {
    }

    @Test
    fun `should calculate commission amount and return error`() = runTest {
        calculateCommissionAndSubmitTransactionUseCase =
            CalculateCommissionAndSubmitTransactionUseCase(
                currencyExchangerRepository = currencyExchangerRepository
            )

        whenever(currencyExchangerRepository.getTransactionCount()).thenReturn(flow { emit(6) })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR")).thenReturn(flow {
            emit(
                300.0
            )
        })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("USD")).thenReturn(flow {
            emit(
                20.0
            )
        })

        val result = calculateCommissionAndSubmitTransactionUseCase(300.0, "EUR", "USD", 50.0)
        result.collect { emission ->
            Assert.assertTrue(emission is Resource.Error && emission.message == "Insufficient funds to pay the fee")
        }
    }

    @Test
    fun `should calculate commission amount and return success`() = runTest {
        calculateCommissionAndSubmitTransactionUseCase =
            CalculateCommissionAndSubmitTransactionUseCase(
                currencyExchangerRepository = currencyExchangerRepository
            )

        whenever(currencyExchangerRepository.getTransactionCount()).thenReturn(flow { emit(6) })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR")).thenReturn(flow {
            emit(
                400.0
            )
        })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("USD")).thenReturn(flow {
            emit(
                20.0
            )
        })

        val transaction = Transaction(
            id = null,
            toCurrency = "USD",
            fromCurrency = "EUR",
            sellAmount = 300.0,
            receiveAmount = 50.0,
            commissionFee = 2.1

        )

        val result = calculateCommissionAndSubmitTransactionUseCase(300.0, "EUR", "USD", 50.0)

        result.collect { emission ->
            Assert.assertTrue(emission is Resource.Success && emission.data == transaction)
        }
    }

    @Test
    fun `should calculate commission amount with transaction that less then 5 and return free commission`() = runTest {
        calculateCommissionAndSubmitTransactionUseCase =
            CalculateCommissionAndSubmitTransactionUseCase(
                currencyExchangerRepository = currencyExchangerRepository
            )

        whenever(currencyExchangerRepository.getTransactionCount()).thenReturn(flow { emit(1) })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR")).thenReturn(flow {
            emit(
                400.0
            )
        })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("USD")).thenReturn(flow {
            emit(
                20.0
            )
        })

        val transaction = Transaction(
            id = null,
            toCurrency = "USD",
            fromCurrency = "EUR",
            sellAmount = 300.0,
            receiveAmount = 50.0,
            commissionFee = 0.0

        )

        val result = calculateCommissionAndSubmitTransactionUseCase(300.0, "EUR", "USD", 50.0)

        result.collect { emission ->
            Assert.assertTrue(emission is Resource.Success && emission.data == transaction)
        }
    }

    @Test
    fun `should calculate commission amount with transaction that multiple of 10 and return free commission`() = runTest {
        calculateCommissionAndSubmitTransactionUseCase =
            CalculateCommissionAndSubmitTransactionUseCase(
                currencyExchangerRepository = currencyExchangerRepository
            )

        whenever(currencyExchangerRepository.getTransactionCount()).thenReturn(flow { emit(10) })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR")).thenReturn(flow {
            emit(
                400.0
            )
        })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("USD")).thenReturn(flow {
            emit(
                20.0
            )
        })

        val transaction = Transaction(
            id = null,
            toCurrency = "USD",
            fromCurrency = "EUR",
            sellAmount = 300.0,
            receiveAmount = 50.0,
            commissionFee = 0.0

        )

        val result = calculateCommissionAndSubmitTransactionUseCase(300.0, "EUR", "USD", 50.0)

        result.collect { emission ->
            Assert.assertTrue(emission is Resource.Success && emission.data == transaction)
        }
    }

    @Test
    fun `should calculate commission amount with transaction that EUR sell amount less than 200 and return free commission`() = runTest {
        calculateCommissionAndSubmitTransactionUseCase =
            CalculateCommissionAndSubmitTransactionUseCase(
                currencyExchangerRepository = currencyExchangerRepository
            )

        whenever(currencyExchangerRepository.getTransactionCount()).thenReturn(flow { emit(7) })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR")).thenReturn(flow {
            emit(
                400.0
            )
        })
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("USD")).thenReturn(flow {
            emit(
                20.0
            )
        })

        val transaction = Transaction(
            id = null,
            toCurrency = "USD",
            fromCurrency = "EUR",
            sellAmount = 100.0,
            receiveAmount = 50.0,
            commissionFee = 0.0

        )

        val result = calculateCommissionAndSubmitTransactionUseCase(100.0, "EUR", "USD", 50.0)

        result.collect { emission ->
            Assert.assertTrue(emission is Resource.Success && emission.data == transaction)
        }
    }
}