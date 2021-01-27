package com.spiraclesoftware.androidsample.domain.entity

interface Identifiable<out T> where T : UniqueIdentifier<*> {

    fun getUniqueId(): T
}