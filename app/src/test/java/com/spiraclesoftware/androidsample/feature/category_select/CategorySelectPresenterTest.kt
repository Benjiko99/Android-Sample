package com.spiraclesoftware.androidsample.feature.category_select

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.domain.entity.TransactionCategory
import com.spiraclesoftware.androidsample.domain.interactor.TransactionsInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class CategorySelectPresenterTest : PresenterTest() {

    @Mock
    private lateinit var transactionsInteractor: TransactionsInteractor

    @Mock
    private lateinit var categoryModelFormatter: CategoryModelFormatter

    private lateinit var presenter: CategorySelectPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = CategorySelectPresenter(
            transactionsInteractor,
            categoryModelFormatter
        )
    }

    @Test
    fun `Models are presented correctly`() = runBlockingTest {
        val categories = listOf(mock<TransactionCategory>())
        val model = mock<CategoryModel>()

        whenever(transactionsInteractor.getAllCategories()) doReturn categories
        whenever(categoryModelFormatter.format(any(), any())) doReturn model

        val models = presenter.getListModels(TransactionCategory.ENTERTAINMENT)
        val expectedModels = listOf(model)

        assertEquals(expectedModels, models)
    }

}
