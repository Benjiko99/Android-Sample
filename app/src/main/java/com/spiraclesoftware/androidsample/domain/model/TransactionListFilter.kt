package com.spiraclesoftware.androidsample.domain.model

data class TransactionListFilter(
    val directionFilter: TransferDirectionFilter
)

fun List<Transaction>.applyFilter(filter: TransactionListFilter): List<Transaction> {
    return if (filter.directionFilter != TransferDirectionFilter.ALL) {
        filter { filter.directionFilter.mapsTo(it.transferDirection) }
    } else this
}