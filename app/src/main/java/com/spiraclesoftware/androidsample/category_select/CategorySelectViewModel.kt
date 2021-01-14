package com.spiraclesoftware.androidsample.category_select

import co.zsmb.rainbowcake.base.QueuedOneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
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

    private val selectedCategory = MutableStateFlow(initialCategory)

    init {
        executeNonBlocking {
            collectSelectedCategory()
        }
    }

    private suspend fun collectSelectedCategory() {
        selectedCategory.collect { category ->
            viewState = (viewState as Content).copy(
                listItems = presenter.getListItems(selectedCategory = category)
            )
        }
    }

    fun selectCategory(category: TransactionCategory) {
        if (category != selectedCategory.value) {
            val oldCategory = selectedCategory.value

            try {
                tryUpdateCategory(category)
            } catch (e: Exception) {
                selectedCategory.value = oldCategory
                postQueuedEvent(NotifyOfFailureEvent)
                Timber.e(e)
            }
        }
    }

    private fun tryUpdateCategory(category: TransactionCategory) = execute {
        selectedCategory.value = category
        presenter.updateCategory(transactionId, category)
        postQueuedEvent(NotifyOfSuccessEvent)
    }

}
