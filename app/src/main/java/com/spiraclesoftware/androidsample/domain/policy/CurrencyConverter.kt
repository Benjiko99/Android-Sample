package com.spiraclesoftware.androidsample.domain.policy

import com.spiraclesoftware.androidsample.SampleApplication
import com.spiraclesoftware.androidsample.domain.interactor.ConversionRatesInteractor
import com.spiraclesoftware.androidsample.domain.model.ConversionRate
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.Money
import java.math.BigDecimal
import java.util.*

class CurrencyConverter(
    private val conversionRatesInteractor: ConversionRatesInteractor
) {

    suspend fun convert(money: Money, to: Currency): Money {
        val convertedAmount = convert(money.amount, money.currency, to)
        return Money(convertedAmount, to)
    }

    private suspend fun convert(amount: BigDecimal, from: Currency, to: Currency): BigDecimal {
        if (from == to) return amount

        val conversionRate = findConversionRate(from, to).rate
        return convert(amount, conversionRate.toBigDecimal())
    }

    private fun convert(amount: BigDecimal, rate: BigDecimal): BigDecimal {
        return amount.multiply(rate, SampleApplication.mathContext)
    }

    private suspend fun findConversionRate(from: Currency, to: Currency): ConversionRate {
        val conversionRates = conversionRatesInteractor.getConversionRates(from)
        return findConversionRate(conversionRates, to)
    }

    private fun findConversionRate(conversionRates: ConversionRates, to: Currency): ConversionRate {
        return conversionRates.findByCurrency(to)
            ?: throw MissingConversionRateException(missingCurrency = to)
    }

    class MissingConversionRateException(missingCurrency: Currency) : RuntimeException(
        "Missing conversion rate for ${missingCurrency.currencyCode} currency."
    )

}