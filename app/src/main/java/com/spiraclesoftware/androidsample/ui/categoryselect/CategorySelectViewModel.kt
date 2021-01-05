package com.spiraclesoftware.androidsample.ui.categoryselect

import co.zsmb.rainbowcake.base.QueuedOneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import timber.log.Timber

class CategorySelectViewModel(
    private val transactionId: TransactionId,
    initialCategory: TransactionCategory,
    private val presenter: CategorySelectPresenter
) : RainbowCakeViewModel<CategorySelectViewState>(
    Content(listItems = emptyList())
) {

    object NotifyOfSuccessEvent : QueuedOneShotEvent
    object NotifyOfFailureEvent : QueuedOneShotEvent

    private var selectedCategory: TransactionCategory = initialCategory

    init {
        updateListItems()
    }

    fun selectCategory(category: TransactionCategory) {
        if (category != selectedCategory) {
            execute {
                val oldCategory = selectedCategory

                try {
                    setSelectedCategory(category)
                    presenter.updateCategory(transactionId, category)
                    postQueuedEvent(NotifyOfSuccessEvent)
                } catch (e: Exception) {
                    Timber.e(e)
                    setSelectedCategory(oldCategory)
                    postQueuedEvent(NotifyOfFailureEvent)
                }
            }
        }
    }

    private fun setSelectedCategory(category: TransactionCategory) {
        selectedCategory = category
        updateListItems()
    }

    private fun updateListItems() {
        viewState = (viewState as Content).copy(
            listItems = presenter.getListItems(selectedCategory)
        )
    }

}
