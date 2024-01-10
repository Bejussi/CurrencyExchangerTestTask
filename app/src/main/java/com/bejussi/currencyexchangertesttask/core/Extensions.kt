package com.bejussi.currencyexchangertesttask.core

import com.bejussi.currencyexchangertesttask.data.local.model.BalanceDto
import com.bejussi.currencyexchangertesttask.domain.model.Balance

fun Balance.mapBalanceToBalanceDto(): BalanceDto {
    return BalanceDto(
        currencyCode = this.currencyCode,
        balance = this.balance
    )
}

fun BalanceDto.mapBalanceDtoToBalance(): Balance {
    return Balance(
        currencyCode = this.currencyCode,
        balance = this.balance
    )
}