package com.spiraclesoftware.androidsample.utils

class HandleOnceValue<T>(private val value: T) {

    var isHandled = false
        private set

    fun peak(): T = value

    fun handleOnce(action: (T) -> Unit) {
        if (!isHandled) {
            action(value)
            isHandled = true
        }
    }

}