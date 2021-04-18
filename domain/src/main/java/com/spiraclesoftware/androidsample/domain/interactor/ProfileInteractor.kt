package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult.Invalid
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult.Valid

class ProfileInteractor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val profileUpdateValidator: ProfileUpdateValidator
) {

    fun getProfile(): Profile {
        return localDataSource.getProfile()
    }

    fun updateProfile(
        profile: Profile
    ): UpdateProfileResult {
        try {
            val validationResult = profileUpdateValidator.sanitizeAndValidate(profile)

            return when (validationResult) {
                is Valid -> {
                    val updatedProfile = validationResult.sanitizedProfile
                    localDataSource.saveProfile(updatedProfile)
                    UpdateProfileResult.Success(updatedProfile)
                }
                is Invalid -> {
                    UpdateProfileResult.ValidationsFailed(validationResult.errors)
                }
            }
        } catch (e: Exception) {
            return UpdateProfileResult.Error(e)
        }
    }

    sealed class UpdateProfileResult {
        data class Success(val updatedProfile: Profile) : UpdateProfileResult()

        data class ValidationsFailed(
            val errors: List<ProfileUpdateValidator.Error>
        ) : UpdateProfileResult()

        data class Error(val exception: Exception) : UpdateProfileResult()
    }

}
