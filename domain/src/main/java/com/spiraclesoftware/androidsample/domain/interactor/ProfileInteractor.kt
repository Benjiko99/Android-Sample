package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.entity.Profile
import java.time.ZonedDateTime

class ProfileInteractor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    fun getProfile(): Profile {
        return localDataSource.getProfile()
    }

    fun updateProfile(
        fullName: String,
        dateOfBirth: String,
        phoneNumber: String,
        email: String
    ) {
        val profile = Profile(
            fullName = fullName,
            dateOfBirth = ZonedDateTime.now(),
            // TODO: parse input
            //dateOfBirth = ZonedDateTime.parse(dateOfBirth),
            phoneNumber = phoneNumber,
            email = email
        )

        localDataSource.saveProfile(profile)
    }

}
