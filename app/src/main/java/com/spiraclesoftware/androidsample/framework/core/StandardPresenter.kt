package com.spiraclesoftware.androidsample.framework.core

import com.spiraclesoftware.androidsample.common.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.domain.core.Result
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