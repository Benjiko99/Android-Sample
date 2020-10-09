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
    CategorySelect(listItems = emptyList())
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
                    setProcessing(true)
                    setSelectedCategory(category)
                    presenter.updateCategory(transactionId, category)
                    notifyOfSuccess()
                } catch (e: Exception) {
                    Timber.e(e)
                    notifyOfFailure()
                    setSelectedCategory(oldCategory)
                }
            }
        }
    }

    private fun setSelectedCategory(category: TransactionCategory) {
        selectedCategory = category
        updateListItems()
    }

    private fun updateListItems() {
        viewState = (viewState as CategorySelect).copy(
            listItems = presenter.getListItems(selectedCategory)
        )
    }

    private fun setProcessing(isProcessing: Boolean) {
        viewState = (viewState as CategorySelect).copy(
            isProcessing = isProcessing
        )
    }

    private fun notifyOfSuccess() {
        setProcessing(false)
        postQueuedEvent(NotifyOfSuccessEvent)
    }

    private fun notifyOfFailure() {
        setProcessing(false)
        postQueuedEvent(NotifyOfFailureEvent)
    }

}
