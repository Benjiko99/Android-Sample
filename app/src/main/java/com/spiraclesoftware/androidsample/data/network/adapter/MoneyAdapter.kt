package com.spiraclesoftware.androidsample.data.network.adapter

import com.google.gson.*
import com.spiraclesoftware.androidsample.domain.model.Money
import java.lang.reflect.Type
import java.util.*

class MoneyAdapter : JsonSerializer<Money>, JsonDeserializer<Money> {

    override fun serialize(
        src: Money,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonObject().apply {
            addProperty("amount", src.amount.toPlainString())
            addProperty("currency", src.currency.currencyCode)
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Money {
        val amount = json.asJsonObject.get("amount").asBigDecimal
        val currency = json.asJsonObject.get("currency").asString
        return Money(
            amount,
            Currency.getInstance(currency)
        )
    }
}