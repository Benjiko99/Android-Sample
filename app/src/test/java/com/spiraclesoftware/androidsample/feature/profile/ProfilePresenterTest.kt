package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
    fun getProfileModel() = runBlockingTest {
        val mockProfileModel = mockk<ProfileModel>()

        every { profileFormatter.profileModel(any()) } returns mockProfileModel
        every { profileInteractor.getProfile() } returns mockk()

        val actual = testSubject.getProfileModel()
        assertThat(actual).isEqualTo(mockProfileModel)
    }

}
