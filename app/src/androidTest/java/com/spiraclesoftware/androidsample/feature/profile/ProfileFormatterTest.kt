package com.spiraclesoftware.androidsample.feature.profile

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.format.FormatterTest
import org.junit.Test
import java.time.ZonedDateTime

class ProfileFormatterTest : FormatterTest() {

    @Test
    fun formatProfileModel() {
        val profile = Profile(
            fullName = "John Doe",
            dateOfBirth = ZonedDateTime.parse("1970-01-01T00:00:00+00:00")!!,
            phoneNumber = "+420 123 456 789",
            email = "john.doe@example.com"
        )

        val expected = ProfileModel(
            fullName = "John Doe",
            dateOfBirth = "01 January 1970",
            phoneNumber = "+420 123 456 789",
            email = "john.doe@example.com"
        )

        val formatter = ProfileFormatter()
        val actual = formatter.profileModel(profile)

        assertThat(actual).isEqualTo(expected)
    }

}