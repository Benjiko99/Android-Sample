package com.spiraclesoftware.androidsample.domain.core

interface Identifiable<out T> where T : UniqueIdentifier<*> {

    fun getUniqueId(): T
}