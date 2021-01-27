package com.spiraclesoftware.androidsample.data_local.mapper

import com.spiraclesoftware.androidsample.data_local.entity.MoneyEntity
import com.spiraclesoftware.androidsample.domain.entity.Money

class MoneyEntityMapper : EntityMapper<MoneyEntity, Money> {

    override fun mapToDomain(obj: MoneyEntity): Money {
        return Money(
            amount = obj.amount,
            currency = obj.currency
        )
    }

    override fun mapToEntity(obj: Money): MoneyEntity {
        return MoneyEntity(
            amount = obj.amount,
            currency = obj.currency
        )
    }

}