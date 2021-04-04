package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewModel.ConfirmDiscardChangesEvent
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewModel.ExitEvent
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Editing
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Viewing
import io.mockk.*
import io.mockk.impl.annotations.MockK
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
        val fullName = "John Smith"
        val dateOfBirth = "1.31.2000"
        val phoneNumber = "+420 123 456 789"
        val email = "john.smith@example.com"

        val profileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfileModel() } returns profileModel
        justRun { profilePresenter.updateProfile(any(), any(), any(), any()) }

        val testSubject = newTestSubject()
        testSubject.observeStateAndEvents { stateObserver, _ ->
            testSubject.startEditing()
            testSubject.saveChanges(
                fullName = fullName,
                dateOfBirth = dateOfBirth,
                phoneNumber = phoneNumber,
                email = email
            )

            stateObserver.assertObserved(
                Viewing(profileModel),
                Editing(profileModel),
                // TODO: Can't test that the profileModel has actually changed
                //  because the presenter is mocked and updateProfile() doesn't return
                //  any value (the new profile)
                Viewing(profileModel)
            )
        }

        verify { profilePresenter.updateProfile(fullName, dateOfBirth, phoneNumber, email) }
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
