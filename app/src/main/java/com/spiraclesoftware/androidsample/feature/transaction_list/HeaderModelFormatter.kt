package com.spiraclesoftware.androidsample.feature.transaction_list

import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.formatter.DateTimeFormat
import com.spiraclesoftware.androidsample.formatter.MoneyFormat
import java.time.ZonedDateTime

class HeaderModelFormatter {

    fun format(
        dateTime: ZonedDateTime,
        contributionToBalance: Money
    ) = HeaderModel(
        date = dateTime.format(DateTimeFormat.PRETTY_DATE),
        contribution = MoneyFormat(contributionToBalance)
            .formatSigned(showSignWhenPositive = false)
    )

}