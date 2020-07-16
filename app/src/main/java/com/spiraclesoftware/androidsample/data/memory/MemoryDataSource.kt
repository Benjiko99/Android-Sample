package com.spiraclesoftware.androidsample.data.memory

import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.CurrencyCode
import com.spiraclesoftware.core.data.memory.AssociatedItemMemoryCache
import java.util.*

class MemoryDataSource {

    private val dummyAccount = Account(Currency.getInstance("EUR"))

    private val conversionRatesCache = AssociatedItemMemoryCache<CurrencyCode, ConversionRates>()

    fun getAccount() = dummyAccount

    fun saveConversionRates(baseCurrency: CurrencyCode, rates: ConversionRates) {
        return conversionRatesCache.set(baseCurrency, rates)
    }

    fun getConversionRates(baseCurrency: CurrencyCode): ConversionRates? {
        return conversionRatesCache.get(baseCurrency)
    }

}
