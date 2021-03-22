package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Viewing
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest : ViewModelTest() {

    @MockK
    lateinit var profilePresenter: ProfilePresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private fun newTestSubject() =
        ProfileViewModel(profilePresenter)

    @Test
    fun onInit_produceViewState() = runBlockingTest {
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfileModel() } returns profileModel

        val viewModel = newTestSubject()
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Viewing(profileModel)
            )
        }
    }

}
