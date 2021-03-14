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
            Result.Error(getPresenterException(cause))
        }
    }

    protected fun getPresenterException(it: Exception = Exception()): PresenterException {
        Timber.e(it)
        return PresenterException(exceptionFormatter.format(it))
    }

}