package com.spiraclesoftware.androidsample.domain.interactor

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.spiraclesoftware.androidsample.TestData
import com.spiraclesoftware.androidsample.data.memory.MemoryDataSource
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsInteractorTest {

    companion object {
        private val MOCK_ACCOUNT = TestData.account
    }

    @Mock
    private lateinit var memoryDataSource: MemoryDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Account is loaded correctly from cache`() = runBlockingTest {
        whenever(memoryDataSource.getAccount()) doReturn MOCK_ACCOUNT

        val interactor = AccountsInteractor(memoryDataSource)

        assertEquals(MOCK_ACCOUNT, interactor.getAccount())
    }

}
