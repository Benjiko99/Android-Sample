package com.spiraclesoftware.androidsample.domain.interactor

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.UpdateProfileResult
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileInteractorTest {

    @MockK
    lateinit var localDataSource: LocalDataSource

    @MockK
    lateinit var remoteDataSource: RemoteDataSource

    @MockK
    lateinit var profileUpdateValidator: ProfileUpdateValidator

    @InjectMockKs
    lateinit var testSubject: ProfileInteractor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getProfile_fromCache() = runBlockingTest {
        val mockProfile = mockk<Profile>()

        every { localDataSource.getProfile() } returns mockProfile

        assertThat(testSubject.getProfile()).isEqualTo(mockProfile)
    }

    @Test
    fun `Profile is updated successfully`() = runBlockingTest {
        justRun { localDataSource.saveProfile(any()) }

        val mockProfile = mockk<Profile>()
        every {
            profileUpdateValidator.sanitizeAndValidate(any())
        } returns ValidationResult.Valid(mockProfile)

        val actual = testSubject.updateProfile(mockk())
        val expected = UpdateProfileResult.Success(mockProfile)

        assertThat(actual).isEqualTo(expected)
        verify { localDataSource.saveProfile(mockProfile) }
    }

    @Test
    fun `Updating profile catches exceptions and returns error result`() = runBlockingTest {
        val exception = Exception()
        every { profileUpdateValidator.sanitizeAndValidate(any()) } throws exception

        val actual = testSubject.updateProfile(mockk())
        val expected = UpdateProfileResult.Error(exception)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Updating profile fails if validations don't pass`() = runBlockingTest {
        justRun { localDataSource.saveProfile(any()) }

        val mockErrors = mockk<List<ProfileUpdateValidator.Error>>()
        every {
            profileUpdateValidator.sanitizeAndValidate(any())
        } returns ValidationResult.Invalid(mockErrors)

        val actual = testSubject.updateProfile(mockk())
        val expected = UpdateProfileResult.ValidationsFailed(mockErrors)

        assertThat(actual).isEqualTo(expected)
    }

}
