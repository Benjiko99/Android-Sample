package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewModel.ConfirmDiscardChangesEvent
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewModel.ExitEvent
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Editing
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
        val profileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfileModel() } returns profileModel

        val testSubject = newTestSubject()
        testSubject.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                Viewing(profileModel)
            )
        }
    }

    @Test
    fun startEditing() = runBlockingTest {
        val profileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfileModel() } returns profileModel

        val testSubject = newTestSubject()
        testSubject.observeStateAndEvents { stateObserver, _ ->
            testSubject.startEditing()

            stateObserver.assertObserved(
                Viewing(profileModel),
                Editing(profileModel)
            )
        }
    }

    @Test
    fun saveChanges() = runBlockingTest {
        val profileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfileModel() } returns profileModel

        val testSubject = newTestSubject()
        testSubject.observeStateAndEvents { stateObserver, _ ->
            testSubject.startEditing()
            testSubject.saveChanges(
                fullName = "John Doe"
            )

            stateObserver.assertObserved(
                Viewing(profileModel),
                Editing(profileModel),
                Viewing(profileModel)
            )
        }
    }

    @Test
    fun confirmDiscardChanges() = runBlockingTest {
        val cleanProfileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }
        val changedProfileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfileModel() } returnsMany listOf(
            cleanProfileModel,
            changedProfileModel,
            cleanProfileModel
        )

        val testSubject = newTestSubject()
        testSubject.observeStateAndEvents { stateObserver, eventObserver ->
            testSubject.startEditing()
            testSubject.confirmDiscardChanges()

            stateObserver.assertObserved(
                Viewing(cleanProfileModel),
                Editing(changedProfileModel),
                Viewing(cleanProfileModel)
            )

            eventObserver.assertObserved(ExitEvent)
        }
    }

    @Test
    fun exitScreen_whileViewing() = runBlockingTest {
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfileModel() } returns profileModel

        val testSubject = newTestSubject()
        testSubject.observeStateAndEvents { stateObserver, eventObserver ->
            testSubject.exitScreen()

            stateObserver.assertObserved(
                Viewing(profileModel)
            )

            eventObserver.assertObserved(ExitEvent)
        }
    }

    @Test
    fun exitScreen_whileEditing() = runBlockingTest {
        val profileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfileModel() } returns profileModel

        val testSubject = newTestSubject()
        testSubject.observeStateAndEvents { stateObserver, eventObserver ->
            testSubject.startEditing()
            testSubject.exitScreen()

            stateObserver.assertObserved(
                Viewing(profileModel),
                Editing(profileModel)
            )

            eventObserver.assertObserved(ConfirmDiscardChangesEvent)
        }
    }

}
