package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.Result.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

val <T> Result<T>.data: T?
    get() = (this as? Success)?.data

/**
 * Applies [transform] to the data of a [Result.Success], otherwise returns the original [Result].
 */
suspend inline fun <T, R> Result<T>.mapOnSuccess(crossinline transform: suspend (value: T) -> Result<R>): Result<R> {
    return when (this) {
        is Success -> transform(this.data)
        is Error -> this
        is Loading -> this
    }
}

/**
 * Applies [transform] to the data of a [Result.Success], otherwise returns the original [Result].
 */
inline fun <T, R> Flow<Result<T>>.mapOnSuccess(crossinline transform: suspend (value: T) -> Result<R>): Flow<Result<R>> {
    return map { result ->
        when (result) {
            is Success -> transform(result.data)
            is Error -> result
            is Loading -> result
        }
    }
}

/**
 * Applies [transform] to the exception of a [Result.Error], otherwise returns the original [Result].
 */
inline fun <T> Flow<Result<T>>.mapOnError(crossinline transform: suspend (value: Exception) -> Exception): Flow<Result<T>> {
    return map { result ->
        when (result) {
            is Success -> result
            is Error -> Error(transform(result.exception))
            is Loading -> result
        }
    }
}