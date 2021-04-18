package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.UpdateProfileResult
import com.spiraclesoftware.androidsample.feature.profile.ProfilePresenter.UpdateProfileModel
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
        val updatedProfileModel = mockk<ProfileModel>()

        every { profileFormatter.profileModel(any()) } returns updatedProfileModel

        every { profileInteractor.updateProfile(any()) } returns
                UpdateProfileResult.Success(mockk())

        val actual = testSubject.updateProfile(mockk())
        val expected = UpdateProfileModel.Success(updatedProfileModel)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun updateProfile_validationsFailed() {
        every { profileInteractor.updateProfile(any()) } returns
                UpdateProfileResult.ValidationsFailed(listOf())

        val mockErrors = mockk<ProfileViewState.ValidationErrors>()
        every { profileFormatter.validationErrors(any()) } returns mockErrors

        val actual = testSubject.updateProfile(mockk())
        val expected = UpdateProfileModel.ValidationError(mockErrors)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun updateProfile_error() {
        every { profileInteractor.updateProfile(any()) } returns
                UpdateProfileResult.Error(Exception())

        every { exceptionFormatter.format(any()) } returns "abc"

        val actual = testSubject.updateProfile(mockk())
        val expected = UpdateProfileModel.Error("abc")

        assertThat(actual).isEqualTo(expected)
    }

}
