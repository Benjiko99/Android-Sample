package com.spiraclesoftware.core.data

interface Identifiable<out T> where T : UniqueIdentifier<*> {

    fun getUniqueId(): T
}