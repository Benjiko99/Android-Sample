package com.spiraclesoftware.androidsample.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider

object TestUtils {

    fun createDb(): MainDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()

        return Room.inMemoryDatabaseBuilder(context, MainDatabase::class.java).build()
    }

}