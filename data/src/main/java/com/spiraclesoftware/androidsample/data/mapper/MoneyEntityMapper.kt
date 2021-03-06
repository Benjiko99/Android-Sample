package com.spiraclesoftware.androidsample.data.mapper

import com.spiraclesoftware.androidsample.data_local.entities.MoneyEntity
import com.spiraclesoftware.androidsample.domain.model.Money

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