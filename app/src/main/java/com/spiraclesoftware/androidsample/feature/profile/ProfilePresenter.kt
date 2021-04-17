package com.spiraclesoftware.androidsample.feature.profile

import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.ProfileUpdateData
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.ProfileUpdateResult
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
        data: ProfileUpdateData
    ): ProfileUpdate {
        return when (val result = profileInteractor.updateProfile(data)) {
            is ProfileUpdateResult.Success -> {
                val updatedProfileModel = formatter.profileModel(result.updatedProfile)
                ProfileUpdate.Success(updatedProfileModel)
            }
            is ProfileUpdateResult.ValidationsFailed -> {
                val validationErrors = formatter.validationErrors(result.errors)
                ProfileUpdate.ValidationError(validationErrors)
            }
            is ProfileUpdateResult.Error -> {
                val errorMessage = exceptionFormatter.format(result.exception)
                ProfileUpdate.Error(errorMessage)
            }
        }
    }

    sealed class ProfileUpdate {
        data class Success(val updatedProfileModel: ProfileModel) : ProfileUpdate()

        data class ValidationError(val validationErrors: ValidationErrors) : ProfileUpdate()

        data class Error(val errorMessage: String) : ProfileUpdate()
    }

}