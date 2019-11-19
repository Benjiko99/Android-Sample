package com.spiraclesoftware.androidsample.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Safely handles observables from LiveData for testing.
 */
object LiveDataTestUtil {

    /**
     * Gets the value of a LiveData safely.
     */
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T? {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        return data
    }

    /**
     * For mocking LiveData observers.
     *
     * Usage: `val observer = lambdaMock<(String) -> Unit>()`
     */
    inline fun <reified T> lambdaMock(): T = Mockito.mock(T::class.java)
}
