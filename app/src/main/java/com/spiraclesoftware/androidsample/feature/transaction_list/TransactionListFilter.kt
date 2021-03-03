package com.spiraclesoftware.androidsample.feature.transaction_list

import com.spiraclesoftware.androidsample.domain.entity.Transaction
import com.spiraclesoftware.androidsample.extension.lenientContains
import com.spiraclesoftware.androidsample.feature.transaction_list.TransferDirectionFilter.ALL
import com.spiraclesoftware.androidsample.formatter.DateTimeFormat
import com.spiraclesoftware.androidsample.formatter.MoneyFormatter

data class TransactionListFilter(
    val searchQuery: String = "",
    val directionFilter: TransferDirectionFilter = ALL,
) {

    /** @return whether any filter is being applied */
    fun isActive(): Boolean {
        return searchQuery.isNotEmpty() || directionFilter != ALL
    }

    fun applyTo(list: List<Transaction>): List<Transaction> {
        var result = list

        if (directionFilter != ALL) {
            result = result.filter { directionFilter.mapsTo(it.transferDirection) }
        }

        if (searchQuery.isNotBlank()) {
            val query = searchQuery.trim()
            result = result.filter { it.matchesSearchQuery(query) }
        }

        return result
    }

    private fun Transaction.matchesSearchQuery(query: String): Boolean {
        val matchesName = name.lenientContains(query)
        val matchesMoney = MoneyFormatter().formatSigned(money).lenientContains(query)
        val matchesDate = processingDate.format(DateTimeFormat.PRETTY_DATE).lenientContains(query)

        return matchesName || matchesMoney || matchesDate
    }

}
