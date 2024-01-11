package com.bejussi.currencyexchangertesttask.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyDto(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val base: String,
    val date: String,
    @Embedded val ratesData: RatesDto
)
