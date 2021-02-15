package com.spiraclesoftware.androidsample.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.Money
import com.spiraclesoftware.androidsample.feature.transaction_list.HeaderModel
import com.spiraclesoftware.androidsample.feature.transaction_list.HeaderModelFormatter
import org.junit.Test
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

class HeaderModelFormatterTest : FormatterTest() {

    @Test
    fun format() {
        val day = ZonedDateTime.parse("1970-01-01T00:00:00+00:00")
        val contribution = Money(BigDecimal("100"), Currency.getInstance("EUR"))

        val expected = HeaderModel(
            date = "01 January 1970",
            contribution = "€100"
        )
        val actual = HeaderModelFormatter().format(day, contribution)

        assertThat(actual).isEqualTo(expected)
    }

}