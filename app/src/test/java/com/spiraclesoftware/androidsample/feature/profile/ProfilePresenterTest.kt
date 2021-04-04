package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
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
    fun updateProfile() {
        justRun { profileInteractor.updateProfile(any(), any(), any(), any()) }

        val fullName = "John Smith"
        val dateOfBirth = "1.31.2000"
        val phoneNumber = "+420 123 456 789"
        val email = "john.smith@example.com"

        testSubject.updateProfile(
            fullName = fullName,
            dateOfBirth = dateOfBirth,
            phoneNumber = phoneNumber,
            email = email
        )

        verify { profileInteractor.updateProfile(fullName, dateOfBirth, phoneNumber, email) }
    }

}
