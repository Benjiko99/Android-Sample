package com.spiraclesoftware.core.data

import com.google.gson.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

data class UniqueIdentifier<T>(val value: T)

/**
 * Serializes only the value of the [UniqueIdentifier].
 *
 * De-serializes a primitive value by wrapping it in a [UniqueIdentifier].
 */
class UniqueIdentifierAdapter : JsonSerializer<UniqueIdentifier<*>>, JsonDeserializer<UniqueIdentifier<*>> {

    override fun serialize(
        src: UniqueIdentifier<*>,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(src.value)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): UniqueIdentifier<*> {
        val typeOfId = (typeOfT as ParameterizedType).actualTypeArguments[0]
        val id = context.deserialize<Any>(json, typeOfId)

        return UniqueIdentifier(id)
    }
}