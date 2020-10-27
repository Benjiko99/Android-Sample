package com.spiraclesoftware.androidsample.data.network.adapter

import com.spiraclesoftware.androidsample.domain.model.ConversionRate
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapter

/**
 * Conversions rates are serialized as a Map of the format: `{ "EUR": 0.0375 }`.
 * We want them to be de-serialized as a List: `[{"currency": "EUR", "rate": 0.0375}]`.
 */
@OptIn(ExperimentalStdlibApi::class)
class ConversionRateListAdapter {

    @ToJson
    fun toJson(list: List<ConversionRate>): String {
        val map = mutableMapOf<String, Float>()

        list.forEach {
            map[it.currency.currencyCode] = it.rate
        }

        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<Map<String, Float>>()
        return adapter.toJson(map)
    }

    @FromJson
    fun fromJson(json: Map<String, Float>): List<ConversionRate> {
        return json.map {
            ConversionRate(it.key, it.value)
        }
    }

}