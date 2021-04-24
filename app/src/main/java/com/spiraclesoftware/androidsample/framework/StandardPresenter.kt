package com.spiraclesoftware.androidsample.framework

import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import timber.log.Timber

abstract class StandardPresenter(
    protected val exceptionFormatter: ExceptionFormatter
) {

    suspend fun <T> tryForResult(operation: suspend () -> T): Result<T> {
        return try {
            Result.Success(operation())
        } catch (cause: Exception) {
            Result.Error(getFormattedException(cause))
        }
    }

    /** Logs and formats the [exception] */
    protected fun getFormattedException(exception: Throwable = Exception()): PresenterException {
        Timber.e(exception)
        return PresenterException(exceptionFormatter.format(exception))
    }

}