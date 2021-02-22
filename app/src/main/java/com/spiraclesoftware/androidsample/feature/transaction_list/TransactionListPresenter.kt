package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.Account
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.mapOnError
import com.spiraclesoftware.androidsample.domain.mapOnSuccess
import com.spiraclesoftware.androidsample.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.framework.StandardPresenter
import com.spiraclesoftware.androidsample.util.LanguageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class TransactionListPresenter(
    private val languageManager: LanguageManager,
    private val accountsInteractor: AccountsInteractor,
    private val transactionsInteractor: TransactionsInteractor,
    private val headerModelFormatter: HeaderModelFormatter,
    private val transactionModelFormatter: TransactionModelFormatter,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

    fun toggleLanguageAndRestart() {
        languageManager.toggleLanguageAndRestart()
    }

    suspend fun getAccount(): Account = withIOContext {
        accountsInteractor.getAccount()
    }

    suspend fun refreshTransactions() = withIOContext {
        transactionsInteractor.refreshTransactions()
    }

    suspend fun flowListModels(
        listFilter: Flow<TransactionListFilter>
    ): Flow<Result<List<Model>>> = withIOContext {
        transactionsInteractor.flowTransactions()
            .combine(listFilter) { result, filter ->
                when (result) {
                    is Result.Success -> Result.Success(filter.applyTo(result.data))
                    else -> result
                }
            }
            .mapOnError { getPresenterException(it) }
            .mapOnSuccess { transactions ->
                tryForResult {
                    transactions
                        .sortAndGroupByDay()
                        .mapToModels()
                }
            }
    }

    private fun List<Transaction>.sortAndGroupByDay() =
        this.sortedByDescending { it.processingDate }
            .groupBy { it.processingDate.truncatedTo(ChronoUnit.DAYS) }

    private suspend fun Map<ZonedDateTime, List<Transaction>>.mapToModels(): List<Model> {
        val models = arrayListOf<Model>()

        this.forEach { (day, transactions) ->
            val contribution = accountsInteractor.getContributionToBalance(transactions)

            models += headerModelFormatter.format(day, contribution)
            models += transactionModelFormatter.format(transactions)
        }
        return models
    }

}