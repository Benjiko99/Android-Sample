package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.model.Account
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import java.util.*

interface MemoryDataSource {
    fun getAccount(): Account

    fun saveConversionRates(baseCurrency: Currency, rates: ConversionRates)

    fun getConversionRates(baseCurrency: Currency): ConversionRates?
}