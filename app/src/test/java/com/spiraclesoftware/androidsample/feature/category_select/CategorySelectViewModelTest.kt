package com.spiraclesoftware.androidsample.feature.category_select

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.entity.TransactionId
import com.spiraclesoftware.androidsample.feature.category_select.CategorySelectViewState.Content
import com.spiraclesoftware.androidsample.feature.category_select.item.model.CategoryModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategorySelectViewModelTest : ViewModelTest() {

    @Test
    fun `Data is loaded correctly from presenter upon creation and leads to ready state`() = runBlockingTest {
        val presenter: CategorySelectPresenter = mockk()
        val mockModels = listOf(mockk<CategoryModel>())
        every { presenter.getListModels(any()) } returns mockModels

        val vm = CategorySelectViewModel(TransactionId("1"), TransactionCategory.ENTERTAINMENT, presenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Content(mockModels)
            )
        }
    }

}
