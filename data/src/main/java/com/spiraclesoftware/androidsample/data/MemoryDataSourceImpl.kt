package com.spiraclesoftware.androidsample.data

import com.spiraclesoftware.androidsample.domain.MemoryDataSource
import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import com.spiraclesoftware.androidsample.domain.model.CurrencyCode
import com.spiraclesoftware.androidsample.domain.model.currencyCode
import java.util.*

class MemoryDataSourceImpl : MemoryDataSource {

    private val dummyAccount = Account(Currency.getInstance("EUR"))

    private val conversionRatesCache = AssociatedItemCache<CurrencyCode, ConversionRates>()

    override fun getAccount() = dummyAccount

    override fun saveConversionRates(baseCurrency: Currency, rates: ConversionRates) {
        return conversionRatesCache.set(baseCurrency.currencyCode(), rates)
    }

    override fun getConversionRates(baseCurrency: Currency): ConversionRates? {
        return conversionRatesCache.get(baseCurrency.currencyCode())
    }

}
