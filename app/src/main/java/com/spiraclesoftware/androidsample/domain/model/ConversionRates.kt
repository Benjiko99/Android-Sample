package com.spiraclesoftware.androidsample.domain.model

import com.squareup.moshi.JsonClass
import org.threeten.bp.ZonedDateTime
import java.util.*

@JsonClass(generateAdapter = true)
data class ConversionRates(
    val baseCurrency: Currency,
    val validityDate: ZonedDateTime,
    val rates: List<ConversionRate>
) : Identifiable<CurrencyCode> {

    override fun getUniqueId() = baseCurrency.currencyCode()

    /**
     * @return the conversion rate for a given currency
     */
    fun findByCurrency(currency: Currency): ConversionRate? {
        return if (currency == baseCurrency) {
            ConversionRate(baseCurrency, 1f)
        } else {
            rates.find { it.currency == currency }
        }
    }

}
