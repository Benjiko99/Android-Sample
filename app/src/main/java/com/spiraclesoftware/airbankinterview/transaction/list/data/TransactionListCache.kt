package com.spiraclesoftware.airbankinterview.transaction.list.data

import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.airbankinterview.transaction.list.domain.TransactionId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionListCache @Inject constructor() {

    /** Marks the cache as invalid, to force an update the next time data is requested. */
    var cacheIsDirty = false

    private var cache: HashMap<TransactionId, Transaction>? = null

    fun set(data: List<Transaction>) {
        if (cache == null) {
            cache = LinkedHashMap()
        }
        cache!!.clear()
        cache!!.putAll(data.associate { it.id to it })
        cacheIsDirty = false
    }

    fun get(): List<Transaction>? = cache?.values?.toList()

    fun get(id: TransactionId): Transaction? = cache?.get(id)
}