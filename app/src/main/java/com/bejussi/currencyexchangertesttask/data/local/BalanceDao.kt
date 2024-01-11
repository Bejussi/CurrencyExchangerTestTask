package com.bejussi.currencyexchangertesttask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bejussi.currencyexchangertesttask.data.local.model.BalanceDto
import com.bejussi.currencyexchangertesttask.data.local.model.CurrencyDto
import com.bejussi.currencyexchangertesttask.data.local.model.TransactionDto
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {

    @Query("SELECT * FROM balances")
    fun getBalances(): Flow<List<BalanceDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBalance(balanceDto: BalanceDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBalance(balanceDto: BalanceDto)

    @Query("SELECT balance FROM balances WHERE currencyCode = :currencyCode")
    fun getBalanceAmountByCurrencyCode(currencyCode: String): Flow<Double>

    @Query("SELECT EXISTS(SELECT * FROM balances WHERE currencyCode = :currencyCode)")
    suspend fun checkBalanceIsExist(currencyCode: String): Boolean

    @Query("SELECT COUNT(*) FROM transactions")
    fun getTransactionCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: TransactionDto)

    @Query("SELECT * FROM currency")
    suspend fun getCurrency(): CurrencyDto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currencyDto: CurrencyDto)


}