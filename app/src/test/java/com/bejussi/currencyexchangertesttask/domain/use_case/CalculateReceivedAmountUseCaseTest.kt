package com.bejussi.currencyexchangertesttask.domain.use_case

import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.presentation.main.actions.calculateReceiveAmount.CalculateReceivedAmountResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CalculateReceivedAmountUseCaseTest {

    private val currencyExchangerRepository = mock<CurrencyExchangerRepository>()
    private lateinit var calculateReceivedAmountUseCase: CalculateReceivedAmountUseCase

    @AfterEach
    fun afterEach() {
        Mockito.reset(currencyExchangerRepository)
    }

    @BeforeEach
    fun beforeEach() {
    }

    @Test
    fun `should calculate received amount with sell amount 0 amount and return error`() = runTest {
        calculateReceivedAmountUseCase = CalculateReceivedAmountUseCase(
            currencyExchangerRepository = currencyExchangerRepository
        )
        val result = calculateReceivedAmountUseCase(0.0, "EUR", "USD", 0.0)
        result.collect { emission ->
            assertTrue(emission is CalculateReceivedAmountResult.Error)
        }
    }

    @Test
    fun `should calculate received amount with sell amount bigger than balance and return error`() =
        runTest {
            calculateReceivedAmountUseCase = CalculateReceivedAmountUseCase(
                currencyExchangerRepository = currencyExchangerRepository
            )
            whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR"))
                .thenReturn(flow { emit(50.0) })

            val result = calculateReceivedAmountUseCase(100.0, "EUR", "USD", 2.0)
            result.collect { emission ->
                assertTrue(emission is CalculateReceivedAmountResult.Error)
            }
        }

    @Test
    fun `should calculate received amount with sell currency that equals receive currency and return error`() =
        runTest {
            calculateReceivedAmountUseCase = CalculateReceivedAmountUseCase(
                currencyExchangerRepository = currencyExchangerRepository
            )
            whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR"))
                .thenReturn(flow { emit(100.0) })
            val result = calculateReceivedAmountUseCase(100.0, "EUR", "EUR", 1.0)
            result.collect { emission ->
                assertTrue(emission is CalculateReceivedAmountResult.Error)
            }
        }

    @Test
    fun `should calculate received amount with null rate and return error`() = runTest {
        calculateReceivedAmountUseCase = CalculateReceivedAmountUseCase(
            currencyExchangerRepository = currencyExchangerRepository
        )
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR"))
            .thenReturn(flow { emit(100.0) })
        val result = calculateReceivedAmountUseCase(100.0, "EUR", "USD", null)
        result.collect { emission ->
            assertTrue(emission is CalculateReceivedAmountResult.Error)
        }
    }

    @Test
    fun `should calculate received amount that equals 0 and return error`() = runTest {
        calculateReceivedAmountUseCase = CalculateReceivedAmountUseCase(
            currencyExchangerRepository = currencyExchangerRepository
        )
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR"))
            .thenReturn(flow { emit(100.0) })
        val result = calculateReceivedAmountUseCase(100.0, "EUR", "USD", 0.0)
        result.collect { emission ->
            assertTrue(emission is CalculateReceivedAmountResult.Error)
        }
    }

    @Test
    fun `should calculate received amount and return success`() = runTest {
        calculateReceivedAmountUseCase = CalculateReceivedAmountUseCase(
            currencyExchangerRepository = currencyExchangerRepository
        )
        whenever(currencyExchangerRepository.getBalanceAmountByCurrencyCode("EUR"))
            .thenReturn(flow { emit(100.0) })
        val result = calculateReceivedAmountUseCase(100.0, "EUR", "USD", 2.0)
        result.collect { emission ->
            assertTrue(emission is CalculateReceivedAmountResult.Success)
        }
    }
}