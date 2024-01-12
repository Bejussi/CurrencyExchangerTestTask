package com.bejussi.currencyexchangertesttask.data

import com.bejussi.currencyexchangertesttask.core.Resource
import com.bejussi.currencyexchangertesttask.core.mapBalanceDtoToBalance
import com.bejussi.currencyexchangertesttask.core.mapBalanceToBalanceDto
import com.bejussi.currencyexchangertesttask.core.mapCurrencyDtoToCurrencyResponce
import com.bejussi.currencyexchangertesttask.core.mapCurrencyResponceDataToCurrencyDto
import com.bejussi.currencyexchangertesttask.core.mapTransactionToTransactionDto
import com.bejussi.currencyexchangertesttask.data.local.BalanceDao
import com.bejussi.currencyexchangertesttask.data.remote.CurrencyExchangeRatesApi
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.CurrencyResponce
import com.bejussi.currencyexchangertesttask.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class CurrencyExchangerRepositoryImpl(
    private val balanceDao: BalanceDao,
    private val currencyExchangeRatesApi: CurrencyExchangeRatesApi
) : CurrencyExchangerRepository {

    override fun getBalances(): Flow<List<Balance>> {
        return balanceDao.getBalances().map { balancesList ->
            balancesList.map { balance ->
                balance.mapBalanceDtoToBalance()
            }
        }
    }

    override suspend fun getRates(): Flow<Resource<CurrencyResponce>> = flow {
        try {
            val data = currencyExchangeRatesApi.getRates()
            balanceDao.insertCurrency(data.mapCurrencyResponceDataToCurrencyDto())
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "An error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Check Internet"))
        }
        val newCurrencyData = balanceDao.getCurrency().mapCurrencyDtoToCurrencyResponce()
        if (newCurrencyData != null) {
            emit(Resource.Success(data = newCurrencyData))
        } else {
            emit(Resource.Error("Check Internet"))
        }
    }

    override suspend fun addBalance(balance: Balance) {
        val mappedBalance = balance.mapBalanceToBalanceDto()
        val isBalanceExist = balanceDao.checkBalanceIsExist(mappedBalance.currencyCode)

        if (isBalanceExist) {
            balanceDao.updateBalance(mappedBalance)
        } else {
            balanceDao.addBalance(mappedBalance)
        }
    }

    override fun getBalanceAmountByCurrencyCode(currencyCode: String): Flow<Double> {
        return balanceDao.getBalanceAmountByCurrencyCode(currencyCode = currencyCode)
    }

    override fun getTransactionCount(): Flow<Int> {
        return balanceDao.getTransactionCount()
    }

    override suspend fun addTransaction(transaction: Transaction) {
        balanceDao.addTransaction(transaction = transaction.mapTransactionToTransactionDto())
    }
}