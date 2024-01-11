package com.bejussi.currencyexchangertesttask.core

import com.bejussi.currencyexchangertesttask.domain.model.Transaction

sealed class UIEvent {
    data class ShowToast(val message: String): UIEvent()
    data class ShowSuccessDialog(val transaction: Transaction): UIEvent()
}
