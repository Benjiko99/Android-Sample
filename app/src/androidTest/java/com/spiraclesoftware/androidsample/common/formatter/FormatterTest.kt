package com.spiraclesoftware.androidsample.common.formatter

import androidx.test.platform.app.InstrumentationRegistry
import org.koin.test.KoinTest

abstract class FormatterTest : KoinTest {

    protected val context = InstrumentationRegistry.getInstrumentation().targetContext!!

}