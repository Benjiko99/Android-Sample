package com.spiraclesoftware.androidsample.data.mapper

import com.spiraclesoftware.androidsample.data_remote.model.ConversionRatesDto
import com.spiraclesoftware.androidsample.domain.model.ConversionRates

class ConversionRatesMapper(
    private val conversionRateMapper: ConversionRateMapper
) : Mapper<ConversionRatesDto, ConversionRates> {

    override fun mapToDomain(obj: ConversionRatesDto): ConversionRates {
        return with(obj) {
            ConversionRates(
                baseCurrency = baseCurrency,
                validityDate = validityDate,
                rates = rates.map { conversionRateMapper.mapToDomain(it) }
            )
        }
    }

    override fun mapToRemote(obj: ConversionRates): ConversionRatesDto {
        return with(obj) {
            ConversionRatesDto(
                baseCurrency = baseCurrency,
                validityDate = validityDate,
                rates = rates.map { conversionRateMapper.mapToRemote(it) }
            )
        }
    }

}