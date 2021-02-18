package com.spiraclesoftware.androidsample.feature.category_select

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategorySelectPresenterTest : PresenterTest() {

    @MockK
    lateinit var transactionsInteractor: TransactionsInteractor

    @MockK
    lateinit var categoryModelFormatter: CategoryModelFormatter

    @InjectMockKs
    lateinit var presenter: CategorySelectPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Models are presented correctly`() = runBlockingTest {
        val categories = listOf(mockk<TransactionCategory>())
        val model = mockk<CategoryModel>()

        every { transactionsInteractor.getAllCategories() } returns categories
        every { categoryModelFormatter.format(any(), any()) } returns model

        val models = presenter.getListModels(TransactionCategory.ENTERTAINMENT)
        val expectedModels = listOf(model)

        assertThat(models).isEqualTo(expectedModels)
    }

}
