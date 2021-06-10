package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.common.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.domain.core.Result
import com.spiraclesoftware.androidsample.domain.core.mapOnError
import com.spiraclesoftware.androidsample.domain.core.mapOnSuccess
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionsFilter
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.framework.core.Model
import com.spiraclesoftware.androidsample.framework.core.StandardPresenter
import com.spiraclesoftware.androidsample.framework.utils.LanguageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class TransactionListPresenter(
    private val languageManager: LanguageManager,
    private val accountsInteractor: AccountsInteractor,
    private val transactionsInteractor: TransactionsInteractor,
    private val formatter: TransactionListFormatter,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

    suspend fun refreshTransactions() = withIOContext {
        transactionsInteractor.refreshTransactions()
    }

    fun toggleLanguageAndRestart() {
        languageManager.toggleLanguageAndRestart()
    }

    fun getFilterStringIds() =
        formatter.filterStringIds()

    suspend fun flowContentModel(
        filterFlow: Flow<TransactionsFilter>
    ): Flow<Result<ContentModel>> = withIOContext {
        transactionsInteractor.flowTransactions(filterFlow)
            .mapOnError { getFormattedException(it) }
            .mapOnSuccess { transactions ->
                tryForResult {
                    val listModels = transactions
                        .sortAndGroupByDay()
                        .mapToModels()

                    val filter = filterFlow.first()
                    val filterModel = formatter.filterModel(filter)

                    val emptyState = formatter.emptyState(listModels.isEmpty(), filter.isActive())

                    ContentModel(listModels, filterModel, emptyState)
                }
            }
    }

    private fun List<Transaction>.sortAndGroupByDay() =
        sortedByDescending { it.processingDate }
            .groupBy { it.processingDate.truncatedTo(ChronoUnit.DAYS) }

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun Map<ZonedDateTime, List<Transaction>>.mapToModels(): List<Model> {
        return flatMap { (day, transactions) ->
            val contribution = accountsInteractor.getContributionToBalance(transactions)

            buildList {
                add(formatter.headerModel(day, contribution))
                addAll(formatter.transactionModel(transactions))
            }
        }
    }

}