package com.bejussi.currencyexchangertesttask.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun sendUiEvent(uiEvent: UIEvent) {
        viewModelScope.launch {
            _eventFlow.send(uiEvent)
        }
    }
}