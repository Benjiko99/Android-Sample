package com.spiraclesoftware.androidsample.shared.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spiraclesoftware.androidsample.application.data.ApiService
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.androidsample.shared.domain.CurrencyCode
import com.spiraclesoftware.core.data.*
import com.spiraclesoftware.core.testing.OpenForTesting

@OpenForTesting
class ConversionRatesRepository(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val cache: AssociatedItemCache<CurrencyCode, ConversionRates>
) {

    fun getConversionRates(baseCurrency: CurrencyCode): LiveData<Resource<ConversionRates>> {
        return object :
            NetworkBoundResource<ConversionRates, ConversionRates>(appExecutors) {

            override fun saveCallResult(data: ConversionRates) {
                cache.set(baseCurrency, data)
            }

            override fun shouldFetch(data: ConversionRates?): Boolean {
                return cache.isDirty || data == null
            }

            override fun loadFromCache(): LiveData<ConversionRates> {
                val cachedData = cache.get(baseCurrency)

                return if (cachedData == null) {
                    AbsentLiveData.create()
                } else {
                    MutableLiveData<ConversionRates>().apply { value = cachedData }
                }
            }

            override fun createCall() = apiService.conversionRates(baseCurrency.value)

            override fun onFetchFailed() {
                cache.isDirty = true
            }
        }.asLiveData()
    }

    fun dirtyCache() {
        cache.isDirty = true
    }
}