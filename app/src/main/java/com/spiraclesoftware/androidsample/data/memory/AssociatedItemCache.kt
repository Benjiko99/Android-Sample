package com.spiraclesoftware.androidsample.data.memory

import com.spiraclesoftware.androidsample.domain.model.Identifiable
import com.spiraclesoftware.androidsample.domain.model.UniqueIdentifier

/**
 * Provides a generic cache for data, mapped to an [Identifiable] key.
 */
class AssociatedItemCache<Key, Value> where Key : UniqueIdentifier<*> {

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