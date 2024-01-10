package com.bejussi.currencyexchangertesttask.data

import com.bejussi.currencyexchangertesttask.core.mapBalanceDtoToBalance
import com.bejussi.currencyexchangertesttask.data.local.BalanceDao
import com.bejussi.currencyexchangertesttask.domain.CurrencyExchangerRepository
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyExchangerRepositoryImpl(
    private val balanceDao: BalanceDao
): CurrencyExchangerRepository {

    override fun getBalances(): Flow<List<Balance>> {
        return balanceDao.getBalances().map { balancesList ->
            balancesList.map { balance ->
                balance.mapBalanceDtoToBalance()
            }
        }
    }
}