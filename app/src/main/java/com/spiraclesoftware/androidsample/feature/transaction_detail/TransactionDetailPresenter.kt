package com.spiraclesoftware.androidsample.feature.transaction_detail

import android.net.Uri
import androidx.core.net.toUri
import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsFormatter
import com.spiraclesoftware.androidsample.feature.transaction_detail.cards.CardsPresenter
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.Model
import com.spiraclesoftware.androidsample.framework.StandardPresenter
import kotlinx.coroutines.flow.*

class TransactionDetailPresenter(
    private val id: TransactionId,
    private val transactionsInteractor: TransactionsInteractor,
    private val transactionDetailFormatter: TransactionDetailFormatter,
    private val cardsPresenter: CardsPresenter,
    private val cardsFormatter: CardsFormatter,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

    fun flowDetailModel(
        id: TransactionId
    ): Flow<DetailModel> {
        return flowTransactionById(id).map { transaction ->
            transactionDetailFormatter.detailModel(transaction!!)
        }.catch { throw getFormattedException(it) }
    }

    fun flowCardModels(
        id: TransactionId,
        attachmentUploads: Flow<List<Uri>>
    ): Flow<List<Model>> {
        return flowTransactionById(id).combine(attachmentUploads) { transaction, uploads ->
            val cards = cardsPresenter.getCards(transaction!!, uploads)
            cardsFormatter.cardModels(cards)
        }.catch { throw getFormattedException(it) }
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
