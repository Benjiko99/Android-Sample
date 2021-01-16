package com.spiraclesoftware.androidsample.shared

import com.spiraclesoftware.androidsample.domain.model.Transaction
import com.spiraclesoftware.androidsample.shared.TransferDirectionFilter.ALL
import com.spiraclesoftware.androidsample.extensions.lenientContains

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
        val matchesMoney = MoneyFormat(money).formatSigned().lenientContains(query)
        val matchesDate = processingDate.format(DateTimeFormat.PRETTY_DATE).lenientContains(query)

        return matchesName || matchesMoney || matchesDate
    }

}
