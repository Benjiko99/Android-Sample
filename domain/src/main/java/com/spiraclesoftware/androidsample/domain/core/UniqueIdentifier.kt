package com.spiraclesoftware.androidsample.domain.core

data class UniqueIdentifier<T>(val value: T) {
    override fun toString() = value.toString()
}
