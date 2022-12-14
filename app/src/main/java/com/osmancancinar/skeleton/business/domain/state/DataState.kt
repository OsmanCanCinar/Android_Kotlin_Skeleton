package com.osmancancinar.skeleton.business.domain.state

import java.lang.Exception

// Represents different states for the UI
sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}