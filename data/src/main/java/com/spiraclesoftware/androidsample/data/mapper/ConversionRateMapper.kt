package com.spiraclesoftware.androidsample.data.mapper

import com.spiraclesoftware.androidsample.domain.model.ConversionRate
import com.spiraclesoftware.androidsample.data_remote.model.ConversionRateDto

class ConversionRateMapper : Mapper<ConversionRateDto, ConversionRate> {

    override fun mapToDomain(obj: ConversionRateDto): ConversionRate {
        return with(obj) {
            ConversionRate(
                currency = currency,
                rate = rate
            )
        }
    }

    override fun mapToRemote(obj: ConversionRate): ConversionRateDto {
        return with(obj) {
            ConversionRateDto(
                currency = currency,
                rate = rate
            )
        }
    }

}