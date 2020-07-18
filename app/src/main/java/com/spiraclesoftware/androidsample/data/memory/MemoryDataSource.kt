package com.spiraclesoftware.androidsample.data.memory

import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.CurrencyCode
import com.spiraclesoftware.androidsample.domain.model.currencyCode
import java.util.*

class MemoryDataSource {

    private val dummyAccount = Account(Currency.getInstance("EUR"))

    private val conversionRatesCache = AssociatedItemCache<CurrencyCode, ConversionRates>()

    fun getAccount() = dummyAccount

    fun saveConversionRates(baseCurrency: Currency, rates: ConversionRates) {
        return conversionRatesCache.set(baseCurrency.currencyCode(), rates)
    }

    fun getConversionRates(baseCurrency: Currency): ConversionRates? {
        return conversionRatesCache.get(baseCurrency.currencyCode())
    }

}
