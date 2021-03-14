package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import androidx.core.net.toUri
import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.Result
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsFormatter
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.StandardPresenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

class TransactionDetailPresenter(
    private val id: TransactionId,
    private val transactionsInteractor: TransactionsInteractor,
    private val transactionDetailFormatter: TransactionDetailFormatter,
    private val cardsPresenter: CardsPresenter,
    private val cardsFormatter: CardsFormatter,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

    fun flowDetailModel(
        id: TransactionId,
        attachmentUploads: Flow<List<Uri>>
    ): Flow<Result<DetailModel>> {
        return flowTransactionById(id).combine(attachmentUploads) { transaction, uploads ->
            if (transaction != null) tryForResult {
                val cards = cardsPresenter.getCards(transaction, uploads)
                val cardModels = cardsFormatter.cardModels(cards)
                transactionDetailFormatter.detailModel(transaction, cardModels)
            }
            else Result.Error(getPresenterException())
        }
    }

    suspend fun updateNote(note: String) = withIOContext {
        transactionsInteractor.updateTransactionNote(id, note)
    }

    suspend fun removeAttachment(uri: Uri) = withIOContext {
        transactionsInteractor.removeAttachment(id, uri.toString())
    }

    suspend fun uploadAttachment(uri: Uri) = withIOContext {
        transactionsInteractor.uploadAttachment(id, uri.toString())
    }

    suspend fun getAttachments() =
        getTransaction().attachments.map(String::toUri)

    suspend fun getNote() =
        getTransaction().noteToSelf.orEmpty()

    suspend fun getCategory() =
        getTransaction().category

    private fun flowTransactionById(id: TransactionId): Flow<Transaction?> {
        return transactionsInteractor.flowTransactionById(id).distinctUntilChanged()
    }

    private suspend fun getTransaction() = withIOContext {
        transactionsInteractor.getTransactionById(id)!!
    }

}
