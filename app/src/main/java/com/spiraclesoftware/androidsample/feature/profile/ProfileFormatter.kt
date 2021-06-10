package com.spiraclesoftware.androidsample.feature.profile

import android.content.Context
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.EmailIsBlank
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.NameIsBlank
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.PhoneNumberIsBlank
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.*
import com.spiraclesoftware.androidsample.framework.extensions.string
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

    fun validationErrors(errors: List<Error>): ProfileViewState.ValidationErrors {
        val nameErrors = errors.filter { it is NameField }
        val dateOfBirthErrors = errors.filter { it is DateOfBirthField }
        val phoneNumberErrors = errors.filter { it is PhoneNumberField }
        val emailErrors = errors.filter { it is EmailField }

        return ProfileViewState.ValidationErrors(
            fullNameError = validationError(nameErrors.getOrNull(0)),
            dateOfBirthError = validationError(dateOfBirthErrors.getOrNull(0)),
            phoneNumberError = validationError(phoneNumberErrors.getOrNull(0)),
            emailError = validationError(emailErrors.getOrNull(0)),
        )
    }

    private fun validationError(error: Error?): String? {
        return when (error) {
            is NameIsBlank -> ctx.string(R.string.profile__error__name_is_blank)
            is PhoneNumberIsBlank -> ctx.string(R.string.profile__error__phone_number_is_blank)
            is EmailIsBlank -> ctx.string(R.string.profile__error__email_is_blank)
            else -> null
        }
    }

}