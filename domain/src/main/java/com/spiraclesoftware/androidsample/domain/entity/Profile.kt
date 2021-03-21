package com.spiraclesoftware.androidsample.domain.entity

import java.time.ZonedDateTime

data class Profile(
    val fullName: String,
    val dateOfBirth: ZonedDateTime,
    val phoneNumber: String,
    val email: String
)
