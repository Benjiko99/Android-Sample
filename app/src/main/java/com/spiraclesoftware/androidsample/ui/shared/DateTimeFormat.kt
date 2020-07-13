package com.spiraclesoftware.androidsample.ui.shared

import org.threeten.bp.format.DateTimeFormatter

object DateTimeFormat {
    val PRETTY_DATE_TIME = DateTimeFormatter.ofPattern("HH:mm, EE, dd MMMM")!!
    val PRETTY_DATE = DateTimeFormatter.ofPattern("dd MMMM YYYY")!!
}