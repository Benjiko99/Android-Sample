package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewModel.*
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.*
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

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                stateObserver.assertObserved(
                    Viewing(profileModel)
                )
            }
        }
    }

    @Test
    fun startEditing() = runBlockingTest {
        val profileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing()
                )
            }
        }
    }

    @Test
    fun saveChanges_success() = runBlockingTest {
        val fullName = "John Smith"
        val dateOfBirth = "1.31.2000"
        val phoneNumber = "+420 123 456 789"
        val email = "john.smith@example.com"

        val profileModel = mockk<ProfileModel>()
        val updatedProfileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfileModel() } returns profileModel

        every { profilePresenter.updateProfile(any(), any(), any(), any()) } returns
                ProfilePresenter.UpdateProfileResult.Success(updatedProfileModel)

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _, queuedEventsObserver ->
                startEditing()
                saveChanges(fullName, dateOfBirth, phoneNumber, email)

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(),
                    Viewing(updatedProfileModel)
                )

                queuedEventsObserver.assertObserved(
                    NotifyChangesSavedEvent
                )
            }
        }
    }

    @Test
    fun `Event notifying of failure is posted when saving changes fails`() = runBlockingTest {
        val errorMessage = "ERROR MESSAGE"

        every { profilePresenter.getProfileModel() } returns mockk()

        every { profilePresenter.updateProfile(any(), any(), any(), any()) } returns
                ProfilePresenter.UpdateProfileResult.Error(errorMessage)

        with(newTestSubject()) {
            observeStateAndEvents { _, _, queuedEventsObserver ->
                startEditing()
                saveChanges("", "", "", "")

                queuedEventsObserver.assertObserved(
                    NotifySavingChangesFailedEvent(errorMessage)
                )
            }
        }
    }

    @Test
    fun `Remain in editing state when saving changes fails`() = runBlockingTest {
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfileModel() } returns profileModel

        every { profilePresenter.updateProfile(any(), any(), any(), any()) } returns
                ProfilePresenter.UpdateProfileResult.Error("")

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()
                saveChanges("", "", "", "")

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing()
                )
            }
        }
    }

    @Test
    fun `Form errors are reset when saving changes fails`() = runBlockingTest {
        val profileModel = mockk<ProfileModel>()
        val validationErrors = ValidationErrors()

        every { profilePresenter.getProfileModel() } returns profileModel

        every { profilePresenter.updateProfile(any(), any(), any(), any()) } returnsMany listOf(
            ProfilePresenter.UpdateProfileResult.ValidationError(validationErrors),
            ProfilePresenter.UpdateProfileResult.Error("")
        )

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()
                saveChanges("", "", "", "")
                saveChanges("", "", "", "")

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(validationErrors = null),
                    Editing(validationErrors = validationErrors),
                    Editing(validationErrors = null),
                )
            }
        }
    }

    @Test
    fun `Saving changes produces form errors when validations fail`() = runBlockingTest {
        val profileModel = mockk<ProfileModel>()
        val formErrors = mockk<ValidationErrors>()

        every { profilePresenter.getProfileModel() } returns profileModel

        every { profilePresenter.updateProfile(any(), any(), any(), any()) } returns
                ProfilePresenter.UpdateProfileResult.ValidationError(formErrors)

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()
                saveChanges("", "", "", "")

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(),
                    Editing(formErrors)
                )
            }
        }
    }

    @Test
    fun confirmDiscardChanges() = runBlockingTest {
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, eventsObserver ->
                startEditing()
                confirmDiscardChanges()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(),
                    Viewing(profileModel)
                )

                eventsObserver.assertObserved(ExitEvent)
            }
        }
    }

    @Test
    fun exitScreen_whileViewing() = runBlockingTest {
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, eventsObserver ->
                exitScreen()

                stateObserver.assertObserved(
                    Viewing(profileModel)
                )

                eventsObserver.assertObserved(ExitEvent)
            }
        }
    }

    @Test
    fun exitScreen_whileEditing() = runBlockingTest {
        val profileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, eventsObserver ->
                startEditing()
                exitScreen()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing()
                )

                eventsObserver.assertObserved(ConfirmDiscardChangesEvent)
            }
        }
    }

}
