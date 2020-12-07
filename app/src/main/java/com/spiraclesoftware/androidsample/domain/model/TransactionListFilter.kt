package com.spiraclesoftware.androidsample.domain.model

import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter.ALL
import com.spiraclesoftware.androidsample.ui.shared.DateTimeFormat
import com.spiraclesoftware.androidsample.ui.shared.MoneyFormat
import com.spiraclesoftware.core.extensions.lenientContains

data class TransactionListFilter(
    val searchQuery: String = "",
    val directionFilter: TransferDirectionFilter = ALL,
) {

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
