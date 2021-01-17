package com.spiraclesoftware.androidsample.data.mapper

import com.spiraclesoftware.androidsample.data_remote.model.MoneyDto
import com.spiraclesoftware.androidsample.domain.model.Money

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