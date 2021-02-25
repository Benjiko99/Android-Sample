package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.Account
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.mapOnError
import com.spiraclesoftware.androidsample.domain.mapOnSuccess
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewModel.ViewData
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
    private val emptyStateFormatter: EmptyStateFormatter,
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

    suspend fun flowViewData(
        listFilter: Flow<TransactionListFilter>
    ): Flow<Result<ViewData>> = withIOContext {
        transactionsInteractor.flowTransactions()
            .mapOnError { getPresenterException(it) }
            .combine(listFilter) { result, filter ->
                result.mapOnSuccess { transactions ->
                    tryForResult {
                        val filteredTransactions = filter.applyTo(transactions)

                        val listModels = filteredTransactions
                            .sortAndGroupByDay()
                            .mapToModels()

                        val emptyState = emptyStateFormatter.format(listModels.isEmpty(), filter.isActive())

                        ViewData(listModels, filter, emptyState)
                    }
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