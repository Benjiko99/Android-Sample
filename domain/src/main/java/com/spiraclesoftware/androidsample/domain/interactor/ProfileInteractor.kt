package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult.Invalid
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult.Valid
import java.time.LocalDate

class ProfileInteractor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val profileUpdateValidator: ProfileUpdateValidator
) {

    fun getProfile(): Profile {
        return localDataSource.getProfile()
    }

    fun updateProfile(
        profileUpdateData: ProfileUpdateData
    ): ProfileUpdateResult {
        try {
            val result = profileUpdateValidator.sanitizeAndValidate(profileUpdateData)

            return when (result) {
                is Valid -> {
                    val profile = result.sanitizedProfile

                    localDataSource.saveProfile(profile)
                    ProfileUpdateResult.Success(profile)
                }
                is Invalid -> {
                    ProfileUpdateResult.ValidationsFailed(result.errors)
                }
            }
        } catch (e: Exception) {
            return ProfileUpdateResult.Error(e)
        }
    }

    data class ProfileUpdateData(
        val fullName: String,
        val dateOfBirth: String,
        val phoneNumber: String,
        val email: String
    ) {

        fun toProfile() =
            Profile(fullName, LocalDate.parse(dateOfBirth), phoneNumber, email)

    }

    sealed class ProfileUpdateResult {
        data class Success(val updatedProfile: Profile) : ProfileUpdateResult()

        data class ValidationsFailed(
            val errors: List<ProfileUpdateValidator.Error>
        ) : ProfileUpdateResult()

        data class Error(val exception: Exception) : ProfileUpdateResult()
    }

}
