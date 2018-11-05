package com.spiraclesoftware.airbankinterview.transaction.list.data

import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.airbankinterview.transaction.list.domain.TransactionId
import com.spiraclesoftware.core.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class TransactionListCache @Inject constructor() {

    /** Marks the cache as invalid, to force an update the next time data is requested. */
    var isDirty = false

    private var cache: HashMap<TransactionId, Transaction>? = null

    fun clear() {
        isDirty = false
        cache = null
    }

    fun set(data: List<Transaction>) {
        isDirty = false

        cache = LinkedHashMap(data.associate { it.id to it })
    }

    fun get(): List<Transaction>? = cache?.values?.toList()

    fun get(id: TransactionId): Transaction? = cache?.get(id)
}