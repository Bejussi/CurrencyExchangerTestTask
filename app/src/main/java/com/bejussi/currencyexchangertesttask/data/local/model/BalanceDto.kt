package com.bejussi.currencyexchangertesttask.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balances")
data class BalanceDto(
    @PrimaryKey(autoGenerate = false)
    val currencyCode: String,
    val balance: Double
)
