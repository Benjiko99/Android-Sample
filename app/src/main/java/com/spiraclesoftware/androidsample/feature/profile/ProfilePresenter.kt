package com.spiraclesoftware.androidsample.feature.profile

import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.ProfileUpdateData
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.ProfileUpdateResult
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.*
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.ValidationErrors
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.StandardPresenter

class ProfilePresenter(
    private val profileInteractor: ProfileInteractor,
    private val formatter: ProfileFormatter,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

    fun getProfileModel(): ProfileModel {
        return profileInteractor.getProfile().let(formatter::profileModel)
    }

    fun updateProfile(
        fullName: String,
        dateOfBirth: String,
        phoneNumber: String,
        email: String
    ): UpdateProfileResult {
        val data = ProfileUpdateData(fullName, dateOfBirth, phoneNumber, email)
        val result = profileInteractor.updateProfile(data)

        return when (result) {
            is ProfileUpdateResult.Success -> {
                val updatedProfileModel = formatter.profileModel(result.updatedProfile)
                UpdateProfileResult.Success(updatedProfileModel)
            }
            is ProfileUpdateResult.ValidationsFailed -> {
                val errors = result.errors

                val nameErrors = errors.filter { it is NameField }
                val dateOfBirthErrors = errors.filter { it is DateOfBirthField }
                val phoneNumberErrors = errors.filter { it is PhoneNumberField }
                val emailErrors = errors.filter { it is EmailField }

                val validationErrors = ValidationErrors(
                    fullNameError = formatter.validationError(nameErrors.getOrNull(0)),
                    dateOfBirthError = formatter.validationError(dateOfBirthErrors.getOrNull(0)),
                    phoneNumberError = formatter.validationError(phoneNumberErrors.getOrNull(0)),
                    emailError = formatter.validationError(emailErrors.getOrNull(0)),
                )

                UpdateProfileResult.ValidationError(validationErrors)
            }
            is ProfileUpdateResult.Error -> {
                val errorMessage = exceptionFormatter.format(result.exception)
                UpdateProfileResult.Error(errorMessage)
            }
        }
    }

    sealed class UpdateProfileResult {
        data class Success(val updatedProfileModel: ProfileModel) : UpdateProfileResult()
        data class ValidationError(val validationErrors: ValidationErrors) : UpdateProfileResult()
        data class Error(val errorMessage: String) : UpdateProfileResult()
    }

}