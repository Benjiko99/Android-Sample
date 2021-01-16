package com.spiraclesoftware.androidsample.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(mapFunction: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, mapFunction)
}

fun <X, Y> LiveData<X>.switchMap(mapFunction: (X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this, mapFunction)
}