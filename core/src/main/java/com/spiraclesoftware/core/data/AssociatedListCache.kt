package com.spiraclesoftware.core.data

import com.spiraclesoftware.core.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class AssociatedListCache<Key, Value> @Inject constructor()
        where Key : UniqueIdentifier<*>, Value : Identifiable<Key> {

    /** Marks the cache as invalid, to force an update the next time data is requested. */
    var isDirty = false

    private var cache: HashMap<Key, Value>? = null

    fun clear() {
        isDirty = false
        cache = null
    }

    fun set(data: List<Value>) {
        isDirty = false

        cache = LinkedHashMap(data.associate { it.getUniqueId() to it })
    }

    fun get(): List<Value>? = cache?.values?.toList()

    fun get(id: Key): Value? = cache?.get(id)
}