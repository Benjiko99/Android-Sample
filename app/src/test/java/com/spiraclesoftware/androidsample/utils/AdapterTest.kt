package com.spiraclesoftware.androidsample.utils

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.spiraclesoftware.androidsample.application.di.appModule
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 * Base class for testing Gson Adapters.
 */
abstract class AdapterTest : KoinTest {

    protected val gson: Gson by inject()

    @Before
    fun setUp() {
        startKoin { modules(listOf(appModule)) }
    }

    @Test
    abstract fun testDeserialization()

    /** Returns a [JsonReader] for a file in the resources folder */
    fun jsonResource(name: String): JsonReader {
        val inputStream = javaClass.classLoader?.getResource(name)?.openStream()
        return JsonReader(inputStream?.reader())
    }
}