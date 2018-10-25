package com.spiraclesoftware.core.data

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

/**
 * Calls the setValue() method on [MutableLiveData] causing the observers to be notified.
 */
class LiveTrigger : MutableLiveData<Void>() {

    fun trigger() {
        value = value
    }
}

/**
 * Calls the setValue() method on [MediatorLiveData] causing the observers to be notified.
 */
class MediatorLiveTrigger : MediatorLiveData<Void>() {

    fun trigger() {
        value = value
    }
}