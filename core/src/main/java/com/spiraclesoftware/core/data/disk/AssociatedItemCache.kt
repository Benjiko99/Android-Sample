package com.spiraclesoftware.core.data.disk

import com.spiraclesoftware.core.domain.UniqueIdentifier
import com.spiraclesoftware.core.testing.OpenForTesting

@OpenForTesting
class AssociatedItemCache<Key, Value> where Key : UniqueIdentifier<*> {

    /** Marks the cache as invalid, to force an update the next time data is requested. */
    var isDirty = false

    private var cache: HashMap<Key, Value>? = null

    fun clear() {
        isDirty = false
        cache = null
    }

    fun set(id: Key, data: Value) {
        isDirty = false

        if (cache == null) {
            cache = LinkedHashMap()
        }
        cache!![id] = data
    }

    fun get(id: Key): Value? = cache?.get(id)
}