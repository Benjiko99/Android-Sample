package com.spiraclesoftware.androidsample.formatter

import androidx.test.platform.app.InstrumentationRegistry

abstract class FormatterTest {

    protected val context = InstrumentationRegistry.getInstrumentation().targetContext!!

}