package com.spiraclesoftware.androidsample.common.formatter

import androidx.test.platform.app.InstrumentationRegistry

abstract class FormatterTest {

    protected val context = InstrumentationRegistry.getInstrumentation().targetContext!!

}