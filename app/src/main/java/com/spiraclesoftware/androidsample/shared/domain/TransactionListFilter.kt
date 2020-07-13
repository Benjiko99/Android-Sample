package com.spiraclesoftware.androidsample.shared.domain

data class TransactionListFilter(
    val transferDirectionFilter: TransferDirectionFilter
)

fun List<Transaction>.applyFilter(filter: TransactionListFilter): List<Transaction> {
    return if (filter.transferDirectionFilter != TransferDirectionFilter.ALL) {
        filter { filter.transferDirectionFilter.mapsTo(it.transferDirection) }
    } else this
}