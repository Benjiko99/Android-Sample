package com.spiraclesoftware.core.extensions

import android.os.Handler

//region Handler / Runnable
fun Handler.postDelayedForever(
    delayMillis: Long,
    delayFirstRun: Boolean = true,
    function: () -> Unit
) {
    val runnable = object : Runnable {
        override fun run() {
            function()
            postDelayed(this, delayMillis)
        }
    }

    if (delayFirstRun)
        postDelayed(runnable, delayMillis)
    else
        post(runnable)
}
//endregion