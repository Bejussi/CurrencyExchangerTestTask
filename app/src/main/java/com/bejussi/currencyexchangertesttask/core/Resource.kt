package com.bejussi.currencyexchangertesttask.core

sealed class Resource<T>(val data: T?, val message: String?, val isError: Boolean?) {
    class Success<T>(data: T?, isError: Boolean? = null) : Resource<T>(data, null, isError)
    class Error<T>(message: String) : Resource<T>(null, message, null)
}
