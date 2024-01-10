package com.bejussi.currencyexchangertesttask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bejussi.currencyexchangertesttask.data.local.model.BalanceDto
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {

    @Query("SELECT * FROM balances")
    fun getBalances(): Flow<List<BalanceDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBalance(balanceDto: BalanceDto)
}