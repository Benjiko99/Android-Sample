package com.spiraclesoftware.core.domain

interface Identifiable<out T> where T : UniqueIdentifier<*> {

    fun getUniqueId(): T
}