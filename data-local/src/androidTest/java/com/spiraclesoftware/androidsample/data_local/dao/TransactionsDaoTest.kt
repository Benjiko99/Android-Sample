package com.spiraclesoftware.androidsample.data_local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spiraclesoftware.androidsample.data_local.MainDatabase
import com.spiraclesoftware.androidsample.data_local.TestUtils
import com.spiraclesoftware.androidsample.data_local.entity.MoneyEntity
import com.spiraclesoftware.androidsample.data_local.entity.TransactionEntity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.util.*

@RunWith(AndroidJUnit4::class)
class TransactionsDaoTest {

    companion object {
        private val MOCK_TRANSACTION = TransactionEntity(
            "1",
            "Test 1",
            Date().toString(),
            MoneyEntity(BigDecimal.ZERO, Currency.getInstance("EUR")),
            "INCOMING",
            "TRANSFERS",
            "COMPLETED",
            "SUCCESSFUL"
        )
    }

    private lateinit var db: MainDatabase
    private lateinit var dao: TransactionsDao

    @Before
    fun setup() {
        db = TestUtils.createDb()
        dao = db.transactionsDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndGetById() {
        val transaction = MOCK_TRANSACTION

        dao.insert(transaction)
        val retrieved = dao.getById("1")
        assertThat(retrieved, equalTo(transaction))
    }

    @Test
    fun deleteAll() {
        val transaction = MOCK_TRANSACTION

        dao.insert(transaction)
        dao.deleteAll()

        val retrieved = dao.getAll()
        assertThat(retrieved, equalTo(emptyList()))
    }

    @Test
    fun repopulateWith() {
        val transaction1 = MOCK_TRANSACTION
        val transaction2 = MOCK_TRANSACTION
            .copy(id = "2")

        dao.insert(transaction1)
        dao.repopulateWith(listOf(transaction2))

        val retrieved = dao.getAll()
        assertThat(retrieved, equalTo(listOf(transaction2)))
    }

}