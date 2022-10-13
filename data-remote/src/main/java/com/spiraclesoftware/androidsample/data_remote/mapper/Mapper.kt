package com.spiraclesoftware.androidsample.data_remote.mapper

interface Mapper<E, D> {
    fun mapToDomain(obj: E): D
    fun mapToRemote(obj: D): E
}