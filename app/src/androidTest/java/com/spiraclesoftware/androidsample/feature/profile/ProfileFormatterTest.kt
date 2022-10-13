package com.spiraclesoftware.androidsample.feature.profile

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.common.formatter.FormatterTest
import com.spiraclesoftware.androidsample.domain.entity.Profile
import org.junit.Test
import org.koin.core.component.inject
import java.time.LocalDate

class ProfileFormatterTest : FormatterTest() {

    private val formatter by inject<ProfileFormatter>()

    @Test
    fun formatProfileModel() {
        val profile = Profile(
            fullName = "John Doe",
            dateOfBirth = LocalDate.parse("1970-01-01"),
            phoneNumber = "+420 123 456 789",
            email = "john.doe@example.com"
        )

        val expected = ProfileModel(
            fullName = "John Doe",
            dateOfBirth = "1970-01-01",
            phoneNumber = "+420 123 456 789",
            email = "john.doe@example.com"
        )

        val actual = formatter.profileModel(profile)

        assertThat(actual).isEqualTo(expected)
    }

}