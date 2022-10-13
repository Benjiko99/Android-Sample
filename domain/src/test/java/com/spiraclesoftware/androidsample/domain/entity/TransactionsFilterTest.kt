package com.spiraclesoftware.androidsample.domain.entity

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.money
import com.spiraclesoftware.androidsample.domain.transaction
import org.junit.Test
import java.time.ZonedDateTime

class TransactionsFilterTest {

    @Test
    fun filterByAllCriteria() {
        val expected = transaction(
            name = "Hello, World",
            transferDirection = TransferDirection.INCOMING,
        )
        val other = transaction(
            name = "Holá, World",
            transferDirection = TransferDirection.OUTGOING,
        )
        val all = listOf(expected, other)

        val filter = TransactionsFilter(
            searchQuery = "Hello",
            directionFilter = TransferDirectionFilter.INCOMING_ONLY
        )
        val actual = filter.getFiltered(all)

        assertThat(actual).isEqualTo(listOf(expected))
    }

    @Test
    fun filterNothing_returnAll() {
        val expected1 = transaction(transferDirection = TransferDirection.INCOMING)
        val expected2 = transaction(transferDirection = TransferDirection.OUTGOING)
        val all = listOf(expected1, expected2)

        val filter = TransactionsFilter()
        val actual = filter.getFiltered(all)

        assertThat(actual).isEqualTo(all)
    }

    @Test
    fun filterByName() {
        val expected = transaction(name = "Hello, World")
        val other = transaction(name = "Holá, World")
        val all = listOf(expected, other)

        val filter = TransactionsFilter(searchQuery = "Hello")
        val actual = filter.getFiltered(all)

        assertThat(actual).isEqualTo(listOf(expected))
    }

    @Test
    fun filterByTransferDirection() {
        val expected = transaction(transferDirection = TransferDirection.INCOMING)
        val other = transaction(transferDirection = TransferDirection.OUTGOING)
        val all = listOf(expected, other)

        val filter = TransactionsFilter(directionFilter = TransferDirectionFilter.INCOMING_ONLY)
        val actual = filter.getFiltered(all)

        assertThat(actual).isEqualTo(listOf(expected))
    }

    @Test
    fun filterByMoney_amount() {
        val expected = transaction(money = money("123", "EUR"))
        val other = transaction(money = money("456", "EUR"))
        val all = listOf(expected, other)

        val filter = TransactionsFilter(searchQuery = "123")
        val actual = filter.getFiltered(all)

        assertThat(actual).isEqualTo(listOf(expected))
    }

    @Test
    fun filterByMoney_currency() {
        val expected = transaction(money = money("123", "EUR"))
        val other = transaction(money = money("456", "USD"))
        val all = listOf(expected, other)

        val filter = TransactionsFilter(searchQuery = "EUR")
        val actual = filter.getFiltered(all)

        assertThat(actual).isEqualTo(listOf(expected))
    }

    @Test
    fun filterByDate_month() {
        val expected = transaction(processingDate = ZonedDateTime.parse("2020-01-31T00:00:00+00:00"))
        val other = transaction(processingDate = ZonedDateTime.parse("2021-02-03T00:00:00+00:00"))
        val all = listOf(expected, other)

        val filter = TransactionsFilter(searchQuery = "January")
        val actual = filter.getFiltered(all)

        assertThat(actual).isEqualTo(listOf(expected))
    }

    @Test
    fun filterByDate_year() {
        val expected = transaction(processingDate = ZonedDateTime.parse("2020-01-31T00:00:00+00:00"))
        val other = transaction(processingDate = ZonedDateTime.parse("2021-02-03T00:00:00+00:00"))
        val all = listOf(expected, other)

        val filter = TransactionsFilter(searchQuery = "2020")
        val actual = filter.getFiltered(all)

        assertThat(actual).isEqualTo(listOf(expected))
    }

}