package com.spiraclesoftware.core.data

import java.util.concurrent.Executor

/**
 * Executors that run the task instantly in a synchronous manner.
 * Used in unit tests.
 */
class InstantAppExecutors : AppExecutors(instant, instant, instant) {

    companion object {
        private val instant = Executor { it.run() }
    }
}