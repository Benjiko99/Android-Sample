package com.spiraclesoftware.androidsample.data.disk.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spiraclesoftware.androidsample.TestUtils
import com.spiraclesoftware.androidsample.data.disk.MainDatabase
import com.spiraclesoftware.androidsample.data.disk.entities.toRoomEntity
import com.spiraclesoftware.androidsample.domain.model.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.ZonedDateTime
import java.math.BigDecimal
import java.util.*

@RunWith(AndroidJUnit4::class)
class TransactionsDaoTest {

    companion object {
        private val MOCK_TRANSACTION = Transaction(
            TransactionId("1"),
            "Test 1",
            ZonedDateTime.now(),
            Money(BigDecimal.ZERO, Currency.getInstance("EUR")),
            TransferDirection.INCOMING,
            TransactionCategory.TRANSFERS,
            TransactionStatus.COMPLETED,
            TransactionStatusCode.SUCCESSFUL
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
        val transaction = MOCK_TRANSACTION.toRoomEntity()

        dao.insert(transaction)
        val retrieved = dao.getById("1")
        assertThat(retrieved, equalTo(transaction))
    }

    @Test
    fun deleteAll() {
        val transaction = MOCK_TRANSACTION.toRoomEntity()

        dao.insert(transaction)
        dao.deleteAll()

        val retrieved = dao.getAll()
        assertThat(retrieved, equalTo(emptyList()))
    }

    @Test
    fun repopulateWith() {
        val transaction1 = MOCK_TRANSACTION.toRoomEntity()
        val transaction2 = MOCK_TRANSACTION
            .copy(id = TransactionId("2"))
            .toRoomEntity()

        dao.insert(transaction1)
        dao.repopulateWith(listOf(transaction2))

        val retrieved = dao.getAll()
        assertThat(retrieved, equalTo(listOf(transaction2)))
    }

}