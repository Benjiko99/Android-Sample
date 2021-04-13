package com.spiraclesoftware.androidsample.data_local

import com.spiraclesoftware.androidsample.data_local.dao.TransactionsDao
import com.spiraclesoftware.androidsample.data_local.mapper.MoneyEntityMapper
import com.spiraclesoftware.androidsample.data_local.mapper.TransactionEntityMapper
import com.spiraclesoftware.androidsample.data_local.util.AssociatedItemCache
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.*

class RoomDataSource(
    private val transactionsDao: TransactionsDao
) : LocalDataSource {

    private val moneyEntityMapper = MoneyEntityMapper()
    private val transactionEntityMapper = TransactionEntityMapper(moneyEntityMapper)

    private val dummyAccount = Account(Currency.getInstance("EUR"))

    private var profile = Profile(
        fullName = "John Doe",
        dateOfBirth = LocalDate.of(2000, 1, 31),
        phoneNumber = "+420 123 456 789",
        email = "john.doe@example.com"
    )

    private val conversionRatesCache = AssociatedItemCache<CurrencyCode, ConversionRates>()

    override fun getAccount() = dummyAccount

    override fun getProfile() = profile

    override fun saveProfile(profile: Profile) {
        this.profile = profile
    }

    override fun flowTransactions(): Flow<List<Transaction>> {
        val roomItems = transactionsDao.flowAll()
        return roomItems.map { it.map(transactionEntityMapper::mapToDomain) }
    }

    override fun getTransactionById(id: TransactionId): Transaction? {
        val roomItem = transactionsDao.getById(id.value)
        return roomItem?.let(transactionEntityMapper::mapToDomain)
    }

    override fun flowTransactionById(id: TransactionId): Flow<Transaction?> {
        val roomItem = transactionsDao.flowById(id.value)
        return roomItem.map { it?.let(transactionEntityMapper::mapToDomain) }
    }

    override fun saveTransactions(transactions: List<Transaction>) {
        val roomItems = transactions.map(transactionEntityMapper::mapToEntity)
        transactionsDao.repopulateWith(roomItems)
    }

    override fun clearTransactions() {
        transactionsDao.deleteAll()
    }

    override fun updateTransaction(transaction: Transaction) {
        val item = transaction.let(transactionEntityMapper::mapToEntity)
        transactionsDao.update(item)
    }

    override fun updateTransaction(id: TransactionId, update: (Transaction) -> Transaction) {
        getTransactionById(id)?.let {
            updateTransaction(update(it))
        }
    }

    override fun saveConversionRates(baseCurrency: Currency, rates: ConversionRates) {
        return conversionRatesCache.set(baseCurrency.currencyCode(), rates)
    }

    override fun getConversionRates(baseCurrency: Currency): ConversionRates? {
        return conversionRatesCache.get(baseCurrency.currencyCode())
    }

}
