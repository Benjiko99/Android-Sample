package com.spiraclesoftware.androidsample.feature.category_select

import co.zsmb.rainbowcake.withIOContext
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor

class CategorySelectPresenter(
    private val transactionsInteractor: TransactionsInteractor,
    private val categoryModelFormatter: CategoryModelFormatter
) {

    fun getListModels(
        selectedCategory: TransactionCategory?
    ): List<CategoryModel> {
        return transactionsInteractor.getAllCategories().map {
            categoryModelFormatter.format(
                category = it,
                isSelectedCategory = it == selectedCategory
            )
        }
    }

    suspend fun updateCategory(id: TransactionId, category: TransactionCategory) = withIOContext {
        transactionsInteractor.updateTransactionCategory(id, category)
    }

}