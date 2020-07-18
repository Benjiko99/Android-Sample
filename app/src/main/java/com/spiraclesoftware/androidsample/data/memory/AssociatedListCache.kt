package com.spiraclesoftware.androidsample.data.memory

import com.spiraclesoftware.androidsample.domain.model.Identifiable
import com.spiraclesoftware.androidsample.domain.model.UniqueIdentifier

/**
 * Provides a generic cache for [List] data, mapped to an [Identifiable] key.
 */
class AssociatedListCache<Key, Value> where Key : UniqueIdentifier<*>, Value : Identifiable<Key> {

    private var cache: HashMap<Key, Value>? = null

    fun set(data: List<Value>) {
        cache = LinkedHashMap(data.associateBy { it.getUniqueId() })
    }

    fun get(): List<Value>? = cache?.values?.toList()

    fun get(id: Key): Value? = cache?.get(id)

    fun clear() {
        cache = null
    }
}