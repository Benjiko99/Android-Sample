package com.spiraclesoftware.airbankinterview.transaction.detail.data

import com.spiraclesoftware.airbankinterview.transaction.detail.domain.TransactionDetail
import com.spiraclesoftware.airbankinterview.transaction.list.domain.TransactionId
import com.spiraclesoftware.core.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

@OpenForTesting
@Singleton
class TransactionDetailCache @Inject constructor() {

    /** Marks the cache as invalid, to force an update the next time data is requested. */
    var cacheIsDirty = false

    private var cache: HashMap<TransactionId, TransactionDetail>? = null

    fun set(id: TransactionId, data: TransactionDetail) {
        if (cache == null) {
            cache = LinkedHashMap()
        }
        cache!![id] = data
        cacheIsDirty = false
    }

    fun get(id: TransactionId): TransactionDetail? = cache?.get(id)
}