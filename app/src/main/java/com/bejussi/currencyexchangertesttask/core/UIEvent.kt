package com.bejussi.currencyexchangertesttask.core

sealed class UIEvent {
    data class ShowToast(val message: String): UIEvent()
}
