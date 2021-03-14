package com.spiraclesoftware.androidsample.feature.transaction_list

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.Account
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionsFilter
import com.spiraclesoftware.androidsample.domain.interactor.AccountsInteractor
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.domain.mapOnError
import com.spiraclesoftware.androidsample.domain.mapOnSuccess
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.framework.StandardPresenter
import com.spiraclesoftware.androidsample.util.LanguageManager
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

    fun getFilterStringIds(): List<Int> {
        return formatter.filterStringIds()
    }

    suspend fun getAccount(): Account = withIOContext {
        accountsInteractor.getAccount()
    }

    suspend fun flowContentModel(
        filterFlow: Flow<TransactionsFilter>
    ): Flow<Result<ContentModel>> = withIOContext {
        transactionsInteractor.flowTransactions(filterFlow)
            .mapOnError { getPresenterException(it) }
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

    private fun List<Transaction>.sortAndGroupByDay(): Map<ZonedDateTime, List<Transaction>> {
        return sortedByDescending { it.processingDate }
            .groupBy { it.processingDate.truncatedTo(ChronoUnit.DAYS) }
    }

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