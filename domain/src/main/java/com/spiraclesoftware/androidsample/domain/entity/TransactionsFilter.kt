package com.spiraclesoftware.androidsample.domain.entity

import com.spiraclesoftware.androidsample.domain.entity.TransferDirectionFilter.ALL
import com.spiraclesoftware.androidsample.domain.util.lenientContains
import java.time.format.DateTimeFormatter

data class TransactionsFilter(
    val searchQuery: String = "",
    val directionFilter: TransferDirectionFilter = ALL,
) {

    /** @return whether any filter is being applied */
    fun isActive(): Boolean {
        return searchQuery.isNotEmpty() || directionFilter != ALL
    }

    fun getFiltered(transactions: List<Transaction>) =
        transactions.filter(::matches)

    private fun matches(transaction: Transaction): Boolean {
        if (directionFilter != ALL) {
            if (transaction.transferDirection != directionFilter.direction)
                return false
        }

        if (searchQuery.isNotBlank()) {
            val query = searchQuery.trim()
            if (!transaction.matchesSearchQuery(query))
                return false
        }

        return true
    }

    private fun Transaction.matchesSearchQuery(query: String): Boolean {
        val matchesName = name.lenientContains(query)
        val matchesMoney = signedMoney.toString().lenientContains(query)
        val matchesDate = processingDate.format(DATE_FORMAT).lenientContains(query)

        return matchesName || matchesMoney || matchesDate
    }

    companion object {
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("MMMM dd MM yyyy")!!
    }

}

enum class TransferDirectionFilter(
    val direction: TransferDirection?
) {
    ALL(null),
    INCOMING_ONLY(TransferDirection.INCOMING),
    OUTGOING_ONLY(TransferDirection.OUTGOING);
}