package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator
import com.spiraclesoftware.androidsample.feature.profile.ProfilePresenter.UpdateProfileResult
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfilePresenterTest : PresenterTest() {

    @MockK
    lateinit var profileInteractor: ProfileInteractor

    @MockK
    lateinit var profileFormatter: ProfileFormatter

    @MockK
    lateinit var exceptionFormatter: ExceptionFormatter

    @InjectMockKs
    lateinit var testSubject: ProfilePresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getProfileModel() {
        val mockProfileModel = mockk<ProfileModel>()

        every { profileFormatter.profileModel(any()) } returns mockProfileModel
        every { profileInteractor.getProfile() } returns mockk()

        val actual = testSubject.getProfileModel()
        assertThat(actual).isEqualTo(mockProfileModel)
    }

    @Test
    fun updateProfile_success() {
        val updatedProfile = mockk<Profile>()
        val updatedProfileModel = mockk<ProfileModel>()

        val result = ProfileInteractor.ProfileUpdateResult.Success(updatedProfile)

        every { profileInteractor.updateProfile(any()) } returns result
        every { profileFormatter.profileModel(any()) } returns updatedProfileModel

        val actual = testSubject.updateProfile("", "", "", "")

        assertThat(actual).isInstanceOf(UpdateProfileResult.Success::class.java)
        assertThat((actual as UpdateProfileResult.Success).updatedProfileModel)
            .isEqualTo(updatedProfileModel)
    }

    // TODO
    /*@Test
    fun updateProfile_validationError() {
        val validationErrors = listOf<ProfileUpdateValidator.Error>()

        every { profileInteractor.updateProfile(any()) } returns
                ProfileInteractor.ProfileUpdateResult.ValidationsFailed(validationErrors)

        every { profileFormatter.validationError(any()) } returns "error message"

        val actual = testSubject.updateProfile("", "", "", "")

        assertThat(actual).isInstanceOf(UpdateProfileResult.ValidationError::class.java)
        assertThat((actual as UpdateProfileResult.ValidationError).validationErrors).isEqualTo(validationErrors)
    }*/

    @Test
    fun updateProfile_error() {
        val result = ProfileInteractor.ProfileUpdateResult.Error(Exception())

        every { profileInteractor.updateProfile(any()) } returns result
        every { exceptionFormatter.format(any()) } returns "ERROR MESSAGE"

        val actual = testSubject.updateProfile("", "", "", "")

        assertThat(actual).isInstanceOf(UpdateProfileResult.Error::class.java)
        assertThat((actual as UpdateProfileResult.Error).errorMessage)
            .isEqualTo("ERROR MESSAGE")
    }

}
