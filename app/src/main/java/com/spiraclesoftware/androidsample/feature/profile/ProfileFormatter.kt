package com.spiraclesoftware.androidsample.feature.profile

import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.format.DateTimeFormat

class ProfileFormatter {

    fun profileModel(profile: Profile) = with(profile) {
        ProfileModel(
            fullName = fullName,
            dateOfBirth = dateOfBirth.format(DateTimeFormat.PRETTY_DATE),
            phoneNumber = phoneNumber,
            email = email
        )
    }

}