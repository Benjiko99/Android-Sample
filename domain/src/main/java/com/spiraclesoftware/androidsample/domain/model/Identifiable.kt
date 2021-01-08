package com.spiraclesoftware.androidsample.domain.model

interface Identifiable<out T> where T : UniqueIdentifier<*> {

    fun getUniqueId(): T
}