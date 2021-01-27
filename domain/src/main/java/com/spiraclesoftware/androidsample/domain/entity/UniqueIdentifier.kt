package com.spiraclesoftware.androidsample.domain.entity

data class UniqueIdentifier<T>(val value: T) {
    override fun toString() = value.toString()
}
