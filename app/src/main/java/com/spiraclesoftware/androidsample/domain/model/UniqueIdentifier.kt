package com.spiraclesoftware.androidsample.domain.model

data class UniqueIdentifier<T>(val value: T) {
    override fun toString() = value.toString()
}
