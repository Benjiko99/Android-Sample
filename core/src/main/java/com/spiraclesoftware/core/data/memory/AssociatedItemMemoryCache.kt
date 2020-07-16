package com.spiraclesoftware.core.data.memory

import com.spiraclesoftware.core.domain.Identifiable
import com.spiraclesoftware.core.domain.UniqueIdentifier

/**
 * Provides a generic cache for data, mapped to an [Identifiable] key.
 */
class AssociatedItemMemoryCache<Key, Value> where Key : UniqueIdentifier<*> {

    private var cache: HashMap<Key, Value>? = null

    fun set(id: Key, data: Value) {
        if (cache == null) {
            cache = LinkedHashMap()
        }
        cache!![id] = data
    }

    fun get(id: Key): Value? = cache?.get(id)

    fun clear() {
        cache = null
    }
}