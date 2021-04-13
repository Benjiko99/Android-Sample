package com.spiraclesoftware.androidsample.feature.profile

sealed class ProfileViewState {

    data class Viewing(
        val profileModel: ProfileModel
    ) : ProfileViewState()

    data class Editing(
        val validationErrors: ValidationErrors? = null
    ) : ProfileViewState()

    data class ValidationErrors(
        val fullNameError: String? = null,
        val dateOfBirthError: String? = null,
        val phoneNumberError: String? = null,
        val emailError: String? = null,
    )

}
