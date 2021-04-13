package com.spiraclesoftware.androidsample.domain.service.profile_update_validator

import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.*

object NameIsBlank : NameField, Error()

object DateOfBirthIsBlank : DateOfBirthField, Error()

object PhoneNumberIsBlank : PhoneNumberField, Error()

object EmailIsBlank : EmailField, Error()
