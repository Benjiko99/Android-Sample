package com.spiraclesoftware.androidsample.data_local.util

import com.spiraclesoftware.androidsample.domain.entity.Identifiable
import com.spiraclesoftware.androidsample.domain.entity.UniqueIdentifier

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