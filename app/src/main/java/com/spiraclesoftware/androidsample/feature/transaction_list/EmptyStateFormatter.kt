package com.spiraclesoftware.androidsample.feature.transaction_list

import com.spiraclesoftware.androidsample.R

class EmptyStateFormatter {

    fun format(
        listIsEmpty: Boolean,
        filterIsActive: Boolean
    ): EmptyState? {
        if (!listIsEmpty) return null

        return if (filterIsActive) {
            EmptyState(
                image = R.drawable.ic_empty_search_results,
                caption = R.string.empty_state__no_results__caption,
                message = R.string.empty_state__no_results__message
            )
        } else {
            EmptyState(
                caption = R.string.empty_state__no_transactions__caption,
                message = R.string.empty_state__no_transactions__message
            )
        }
    }

}