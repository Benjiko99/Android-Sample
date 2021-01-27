package com.spiraclesoftware.androidsample.feature.category_select

import co.zsmb.rainbowcake.withIOContext
import com.mikepenz.fastadapter.GenericItem
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor

class CategorySelectPresenter(
    private val transactionsInteractor: TransactionsInteractor
) {

    private val allCategories = TransactionCategory.values().toList()

    fun getListItems(
        selectedCategory: TransactionCategory?
    ): List<GenericItem> {
        return allCategories.map {
            CategoryItem(
                category = it,
                isChecked = it == selectedCategory
            )
        }
    }

    suspend fun updateCategory(id: TransactionId, category: TransactionCategory) = withIOContext {
        transactionsInteractor.updateTransactionCategory(id, category)
    }

}