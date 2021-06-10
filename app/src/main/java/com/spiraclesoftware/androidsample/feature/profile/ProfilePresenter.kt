package com.spiraclesoftware.androidsample.feature.profile

import com.spiraclesoftware.androidsample.common.formatter.ExceptionFormatter
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.UpdateProfileResult
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.ValidationErrors
import com.spiraclesoftware.androidsample.framework.core.StandardPresenter

class ProfilePresenter(
    private val profileInteractor: ProfileInteractor,
    private val formatter: ProfileFormatter,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

    fun getProfile(): Profile {
        return profileInteractor.getProfile()
    }

    fun getProfileModel(): ProfileModel {
        return getProfile().let(formatter::profileModel)
    }

    fun updateProfile(
        profile: Profile
    ): UpdateProfileModel {
        val result = profileInteractor.updateProfile(profile)

        return when (result) {
            is UpdateProfileResult.Success -> {
                val updatedProfileModel = formatter.profileModel(result.updatedProfile)
                UpdateProfileModel.Success(updatedProfileModel)
            }
            is UpdateProfileResult.ValidationsFailed -> {
                val validationErrors = formatter.validationErrors(result.errors)
                UpdateProfileModel.ValidationError(validationErrors)
            }
            is UpdateProfileResult.Error -> {
                val errorMessage = exceptionFormatter.format(result.exception)
                UpdateProfileModel.Error(errorMessage)
            }
        }
    }

    sealed class UpdateProfileModel {
        data class Success(val updatedProfileModel: ProfileModel) : UpdateProfileModel()

        data class ValidationError(val validationErrors: ValidationErrors) : UpdateProfileModel()

        data class Error(val errorMessage: String) : UpdateProfileModel()
    }

}