package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.core.domain.Identifiable
import org.threeten.bp.ZonedDateTime
import java.math.BigDecimal
import java.util.*

data class Transaction(
    val id: TransactionId,
    val name: String,
    val processingDate: ZonedDateTime,
    val money: Money,
    val transferDirection: TransferDirection,
    val category: TransactionCategory,
    val status: TransactionStatus,
    val statusCode: TransactionStatusCode,
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

    val formattedMoney: String
        get() = if (contributesToBalance()) {
            signedMoney.formatSigned()
        } else {
            signedMoney.formatUnsigned()
        }

    fun contributesToBalance(): Boolean {
        return statusCode == TransactionStatusCode.SUCCESSFUL
    }

    /**
     * Calculates how much a transaction contributes to the account balance (if anything).
     * Returns contributed amount in account currency.
     */
    fun getContributionToBalance(
        rates: ConversionRates,
        accountCurrency: Currency
    ): Money {
        if (!contributesToBalance()) {
            return Money(
                BigDecimal.ZERO,
                accountCurrency
            )
        }

        return signedMoney.convertToCurrency(accountCurrency.currencyCode(), rates)
    }
}
