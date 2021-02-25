package com.spiraclesoftware.androidsample.formatter

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.feature.transaction_list.EmptyState
import com.spiraclesoftware.androidsample.feature.transaction_list.EmptyStateFormatter
import org.junit.Test

class EmptyStateFormatterTest : FormatterTest() {

    @Test
    fun when_listIsEmptyWithoutFilter_return_emptyState() {
        val listIsEmpty = true
        val filterIsActive = false

        val expected = EmptyState(
            caption = R.string.empty_state__no_transactions__caption,
            message = R.string.empty_state__no_transactions__message
        )
        val actual = EmptyStateFormatter().format(listIsEmpty, filterIsActive)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun when_listIsEmptyAndFiltered_return_emptyState() {
        val listIsEmpty = true
        val filterIsActive = true

        val expected = EmptyState(
            image = R.drawable.ic_empty_search_results,
            caption = R.string.empty_state__no_results__caption,
            message = R.string.empty_state__no_results__message
        )
        val actual = EmptyStateFormatter().format(listIsEmpty, filterIsActive)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun when_listIsNotEmpty_return_nothing() {
        val listIsEmpty = false
        val filterIsActive = false

        val expected = null
        val actual = EmptyStateFormatter().format(listIsEmpty, filterIsActive)

        assertThat(actual).isEqualTo(expected)
    }

}