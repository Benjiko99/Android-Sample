package com.spiraclesoftware.androidsample.data_remote.mapper

import com.spiraclesoftware.androidsample.data_remote.dto.MoneyDto
import com.spiraclesoftware.androidsample.domain.entity.Money

class MoneyMapper : Mapper<MoneyDto, Money> {

    override fun mapToDomain(obj: MoneyDto): Money {
        return Money(
            amount = obj.amount,
            currency = obj.currency
        )
    }

    override fun mapToRemote(obj: Money): MoneyDto {
        return MoneyDto(
            amount = obj.amount,
            currency = obj.currency
        )
    }

}