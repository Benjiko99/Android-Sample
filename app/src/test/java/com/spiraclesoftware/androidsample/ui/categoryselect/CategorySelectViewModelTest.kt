package com.spiraclesoftware.androidsample.ui.categoryselect

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.model.TransactionCategory
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategorySelectViewModelTest : ViewModelTest() {

    companion object {
        private val CATEGORIES = TransactionCategory.values().toList()
        private val MOCK_LIST_ITEMS = CATEGORIES.map { CategoryItem(it, it == TransactionCategory.ENTERTAINMENT) }
    }

    @Test
    fun `Data is loaded correctly from presenter upon creation and leads to ready state`() = runBlockingTest {
        val presenter: CategorySelectPresenter = mock()
        whenever(presenter.getListItems(any())) doReturn MOCK_LIST_ITEMS

        val vm = CategorySelectViewModel(TransactionId(1), TransactionCategory.ENTERTAINMENT, presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                CategorySelect(
                    MOCK_LIST_ITEMS,
                    isProcessing = false
                )
            )
        }
    }

}
