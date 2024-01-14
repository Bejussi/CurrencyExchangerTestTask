package com.bejussi.currencyexchangertesttask.presentation.main.uiEvent

import com.bejussi.currencyexchangertesttask.core.UIEvent
import com.bejussi.currencyexchangertesttask.domain.model.Transaction

interface MainUiEventResult: UIEvent {

    fun apply(mainUiEventActions: MainUiEventActions)

    data class ShowToast(val message: String) : MainUiEventResult {
        override fun apply(mainUiEventActions: MainUiEventActions) {
            mainUiEventActions.showToastMessage(message = message)
        }


    }

    data class ShowSuccessDialog(val transaction: Transaction) : MainUiEventResult {
        override fun apply(mainUiEventActions: MainUiEventActions) {
            mainUiEventActions.showSuccessDialog(transaction = transaction)
        }

    }
}