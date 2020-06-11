package com.spiraclesoftware.androidsample.shared.domain

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

data class Money(
    val amount: BigDecimal,
    val currency: Currency
) {

    fun format(transferDirection: TransferDirection? = null): String {
        val formatter = NumberFormat.getCurrencyInstance().apply {
            currency = this@Money.currency
            minimumFractionDigits = 0
        }

        val currencyAmount = formatter.format(amount)

        return if (transferDirection == null) {
            currencyAmount
        } else {
            "${transferDirection.symbol} $currencyAmount"
        }
    }
}
