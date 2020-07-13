package com.spiraclesoftware.androidsample.data.network.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.spiraclesoftware.androidsample.domain.model.ConversionRate
import com.spiraclesoftware.androidsample.domain.model.ConversionRates
import java.lang.reflect.Type
import java.util.*

class ConversionRatesAdapter : JsonDeserializer<ConversionRates> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ConversionRates {
        val body = json.asJsonObject
        val baseCurrencyObj = body.get("baseCurrency").asString
        val dateObj = body.get("validityDate")
        val ratesObj = body.get("rates").asJsonObject

        return ConversionRates(
            baseCurrency = Currency.getInstance(baseCurrencyObj),
            validityDate = context.deserialize(dateObj, Date::class.java),
            rates = getRates(ratesObj)
        )
    }

    private fun getRates(rates: JsonObject): List<ConversionRate> {
        return rates.entrySet().map {
            ConversionRate(Currency.getInstance(it.key), it.value.asFloat)
        }
    }

}