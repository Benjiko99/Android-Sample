package com.spiraclesoftware.core.data

import com.spiraclesoftware.core.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class AssociatedItemCache<Key, Value> @Inject constructor()
        where Key : UniqueIdentifier<*> {

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