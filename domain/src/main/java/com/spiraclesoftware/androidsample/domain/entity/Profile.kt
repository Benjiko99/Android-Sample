package com.spiraclesoftware.androidsample.domain.entity

import java.time.LocalDate

data class Profile(
    val fullName: String,
    val dateOfBirth: LocalDate,
    val phoneNumber: String,
    val email: String
)
