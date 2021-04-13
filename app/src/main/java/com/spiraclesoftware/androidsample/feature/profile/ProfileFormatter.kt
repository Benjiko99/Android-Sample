package com.spiraclesoftware.androidsample.feature.profile

import android.content.Context
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.*
import com.spiraclesoftware.androidsample.extension.string
import java.time.format.DateTimeFormatter

class ProfileFormatter(private val ctx: Context) {

    fun profileModel(profile: Profile) = with(profile) {
        ProfileModel(
            fullName = fullName,
            dateOfBirth = dateOfBirth.format(DateTimeFormatter.ISO_LOCAL_DATE),
            phoneNumber = phoneNumber,
            email = email
        )
    }

    fun validationError(error: ProfileUpdateValidator.Error?): String? {
        return when (error) {
            is NameIsBlank -> ctx.string(R.string.profile__error__name_is_blank)
            is DateOfBirthIsBlank -> ctx.string(R.string.profile__error__date_of_birth_is_blank)
            is PhoneNumberIsBlank -> ctx.string(R.string.profile__error__phone_number_is_blank)
            is EmailIsBlank -> ctx.string(R.string.profile__error__email_is_blank)
            else -> null
        }
    }

}