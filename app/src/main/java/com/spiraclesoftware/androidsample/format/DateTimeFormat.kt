package com.spiraclesoftware.androidsample.format

import java.time.format.DateTimeFormatter

object DateTimeFormat {

    val PRETTY_DATE_TIME = DateTimeFormatter.ofPattern("HH:mm, EE, dd MMMM")!!
    val PRETTY_DATE = DateTimeFormatter.ofPattern("dd MMMM yyyy")!!

}