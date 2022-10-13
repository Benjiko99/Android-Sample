package com.spiraclesoftware.androidsample.feature.category_select

import co.zsmb.rainbowcake.base.QueuedOneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.category_select.CategorySelectViewState.Content
import com.spiraclesoftware.androidsample.feature.category_select.item.model.CategoryModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class CategorySelectViewModel(
    private val transactionId: TransactionId,
    initialCategory: TransactionCategory,
    private val presenter: CategorySelectPresenter
) : RainbowCakeViewModel<CategorySelectViewState>(
    Content(listModels = emptyList())
) {

    object NotifyOfSuccessEvent : QueuedOneShotEvent
    object NotifyOfFailureEvent : QueuedOneShotEvent

    private val selectedCategory = MutableStateFlow(initialCategory)

    init {
        collectSelectedCategory()
    }

    private fun collectSelectedCategory() = executeNonBlocking {
        selectedCategory.collect { category ->
            val models = presenter.getListModels(selectedCategory = category)
            viewState = Content(models)
        }
    }

    fun onCategoryClicked(model: CategoryModel) {
        val category = TransactionCategory.values()[model.ordinal]
        selectCategory(category)
    }

    private fun selectCategory(category: TransactionCategory) {
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
