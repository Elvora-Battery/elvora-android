package com.unsoed.elvora.data

sealed class ApiResult<out R> {
    data object Loading: ApiResult<Nothing>()
    data class Success<T>(val data: T): ApiResult<T>()
    data class Error(val message: String): ApiResult<Nothing>()
    data object Empty: ApiResult<Nothing>()
}
