package com.spiraclesoftware.androidsample.domain.service.profile_update_validator

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.time.LocalDate
import java.time.Month.JANUARY

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileUpdateValidatorTest {

    @Test
    fun `Validation passes when inputs satisfy all rules`() = runBlockingTest {
        val profile = Profile(
            fullName = "John Smith",
            dateOfBirth = LocalDate.parse("2000-01-31"),
            phoneNumber = "+420 123 456 789",
            email = "john.smith@example.com"
        )
        val actual = ProfileUpdateValidator().sanitizeAndValidate(profile)

        val expectedProfile = Profile(
            fullName = "John Smith",
            dateOfBirth = LocalDate.of(2000, JANUARY, 31),
            phoneNumber = "+420 123 456 789",
            email = "john.smith@example.com"
        )
        val expected = ValidationResult.Valid(expectedProfile)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Inputs are sanitized (trimmed, etc)`() = runBlockingTest {
        val profile = Profile(
            fullName = "  John Smith  ",
            dateOfBirth = LocalDate.MIN,
            phoneNumber = "  +420 123 456 789  ",
            email = "  john.smith@example.com  "
        )
        val actual = ProfileUpdateValidator().sanitizeAndValidate(profile)

        val expectedProfile = Profile(
            fullName = "John Smith",
            dateOfBirth = LocalDate.MIN,
            phoneNumber = "+420 123 456 789",
            email = "john.smith@example.com"
        )
        val expected = ValidationResult.Valid(expectedProfile)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Blank inputs don't pass validations`() = runBlockingTest {
        val data = Profile(
            fullName = "  ",
            dateOfBirth = LocalDate.now(),
            phoneNumber = "  ",
            email = "  "
        )
        val actual = ProfileUpdateValidator().sanitizeAndValidate(data)

        val expectedErrors = listOf(
            NameIsBlank,
            PhoneNumberIsBlank,
            EmailIsBlank,
        )
        val expected = ValidationResult.Invalid(expectedErrors)

        assertThat(actual).isEqualTo(expected)
    }

}