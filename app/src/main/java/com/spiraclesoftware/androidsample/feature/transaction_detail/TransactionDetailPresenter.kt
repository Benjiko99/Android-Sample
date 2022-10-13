package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.common.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory.TRANSFERS
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.entity.TransferDirection.OUTGOING
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.framework.core.Model
import com.spiraclesoftware.androidsample.framework.core.StandardPresenter
import kotlinx.coroutines.flow.*

class TransactionDetailPresenter(
    private val transactionId: TransactionId,
    private val transactionsInteractor: TransactionsInteractor,
    private val transactionDetailFormatter: TransactionDetailFormatter,
    private val cardsPresenter: CardsPresenter,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

    private val transactionFlow: Flow<Transaction?> = transactionsInteractor
        .flowTransactionById(transactionId)
        .distinctUntilChanged()

    private suspend fun getTransaction(): Transaction =
        transactionFlow.first()!!

    fun flowDetailModel(): Flow<DetailModel> {
        return transactionFlow.map { transaction ->
            transactionDetailFormatter.detailModel(transaction!!)
        }.catch { throw getFormattedException(it) }
    }

    fun flowCardModels(
        attachmentUploads: Flow<List<Uri>>
    ): Flow<List<Model>> {
        return transactionFlow.combine(attachmentUploads) { transaction, uploads ->
            cardsPresenter.getCardModels(transaction!!, uploads)
        }.catch { throw getFormattedException(it) }
    }

    suspend fun getActionChips(): List<ActionChip> = listOf(
        ActionChip(
            R.id.action_split_bill,
            isVisible = with(getTransaction()) {
                isSuccessful() && transferDirection == OUTGOING && category != TRANSFERS
            }
        )
    )

    suspend fun updateNote(note: String) = withIOContext {
        transactionsInteractor.updateTransactionNote(transactionId, note)
    }

    suspend fun removeAttachment(uri: String) = withIOContext {
        transactionsInteractor.removeAttachment(transactionId, uri)
    }

    suspend fun uploadAttachment(uri: String) = withIOContext {
        transactionsInteractor.uploadAttachment(transactionId, uri)
    }

    suspend fun getAttachments() =
        getTransaction().attachments

    suspend fun getNote() =
        getTransaction().noteToSelf.orEmpty()

    suspend fun getCategory() =
        getTransaction().category

}
