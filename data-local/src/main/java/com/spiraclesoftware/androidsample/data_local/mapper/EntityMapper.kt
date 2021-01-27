package com.spiraclesoftware.androidsample.data_local.mapper

interface EntityMapper<E, D> {
    fun mapToDomain(obj: E): D
    fun mapToEntity(obj: D): E
}