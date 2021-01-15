package com.spiraclesoftware.androidsample.data.mapper

import com.spiraclesoftware.androidsample.domain.model.Money
import com.spiraclesoftware.androidsample.remote.model.MoneyDto

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