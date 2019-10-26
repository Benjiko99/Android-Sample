package com.spiraclesoftware.androidsample.shared.data.adapter

import com.spiraclesoftware.androidsample.utils.AdapterTest
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates

class ConversionRatesAdapterTest : AdapterTest() {

    override fun testDeserialization() {
        gson.fromJson<ConversionRates>(
            jsonResource("conversion_rates.json"),
            ConversionRates::class.java
        )
    }
}