package com.bejussi.currencyexchangertesttask.presentation.main.uiEvent

import com.bejussi.currencyexchangertesttask.domain.model.Transaction

interface MainUiEventActions {

    fun showToastMessage(message: String)

    fun showSuccessDialog(transaction: Transaction)
}