package com.bejussi.currencyexchangertesttask.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val toCurrency: String,
    val fromCurrency: String,
    val sellAmount: Double,
    val receiveAmount: Double,
    val commissionFee: Double
)
