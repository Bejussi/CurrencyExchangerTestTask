package com.bejussi.currencyexchangertesttask.data

import com.bejussi.currencyexchangertesttask.core.Resource
import com.bejussi.currencyexchangertesttask.core.mapBalanceDtoToBalance
import com.bejussi.currencyexchangertesttask.core.mapCurrencyResponceDataToCurrencyResponce
import com.bejussi.currencyexchangertesttask.data.local.BalanceDao
import com.bejussi.currencyexchangertesttask.data.remote.CurrencyExchangeRatesApi
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.CurrencyResponce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class CurrencyExchangerRepositoryImpl(
    private val balanceDao: BalanceDao,
    private val currencyExchangeRatesApi: CurrencyExchangeRatesApi
): CurrencyExchangerRepository {

    override fun getBalances(): Flow<List<Balance>> {
        return balanceDao.getBalances().map { balancesList ->
            balancesList.map { balance ->
                balance.mapBalanceDtoToBalance()
            }
        }
    }

    override suspend fun getRates(base: String): Flow<Resource<CurrencyResponce>> = flow {
        try {
            val data = currencyExchangeRatesApi.getRates(base = base)
            emit(Resource.Success(data = data.mapCurrencyResponceDataToCurrencyResponce()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "An error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Check Internet"))
        }
    }
}