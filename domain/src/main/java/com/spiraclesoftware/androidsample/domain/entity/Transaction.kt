package com.spiraclesoftware.androidsample.domain.entity

import com.spiraclesoftware.androidsample.domain.core.Identifiable
import java.time.ZonedDateTime

data class Transaction(
    val id: TransactionId,
    val name: String,
    val processingDate: ZonedDateTime,
    val money: Money,
    val transferDirection: TransferDirection,
    val category: TransactionCategory,
    val status: TransactionStatus,
    val statusCode: TransactionStatusCode,
    val attachments: List<String> = emptyList(),
    val cardDescription: String? = null,
    val noteToSelf: String? = null
) : Identifiable<TransactionId> {

    companion object {
        const val MAX_ATTACHMENTS = 5
    }

    override fun getUniqueId() = id

    /**
     * Money in transactions is always absolute and the sign has to be determined by the [TransferDirection].
     * Returns money with the proper sign set.
     */
    val signedMoney
        get() = when (transferDirection) {
            TransferDirection.INCOMING -> money
            TransferDirection.OUTGOING -> money.negate()
        }

    fun isSuccessful(): Boolean {
        val successful = statusCode == TransactionStatusCode.SUCCESSFUL
        val completed = status == TransactionStatus.COMPLETED
        return successful && completed
    }

    fun contributesToAccountBalance(): Boolean {
        return isSuccessful()
    }

}
