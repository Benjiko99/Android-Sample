package com.spiraclesoftware.androidsample.domain.model

import android.net.Uri
import com.spiraclesoftware.androidsample.domain.policy.negate
import com.squareup.moshi.JsonClass
import org.threeten.bp.ZonedDateTime

@JsonClass(generateAdapter = true)
data class Transaction(
    val id: TransactionId,
    val name: String,
    val processingDate: ZonedDateTime,
    val money: Money,
    val transferDirection: TransferDirection,
    val category: TransactionCategory,
    val status: TransactionStatus,
    val statusCode: TransactionStatusCode,
    val attachments: List<Uri> = emptyList(),
    val cardDescription: String? = null,
    val noteToSelf: String? = null
) : Identifiable<TransactionId> {

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

}
