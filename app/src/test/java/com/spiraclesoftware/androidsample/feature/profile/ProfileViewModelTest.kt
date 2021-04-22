package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.feature.profile.ProfilePresenter.UpdateProfileModel
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
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest : ViewModelTest() {

    companion object {
        val PROFILE = Profile(
            fullName = "John Doe",
            dateOfBirth = LocalDate.parse("1970-01-01"),
            phoneNumber = "+420 123 456 789",
            email = "john.doe@example.com"
        )
    }

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
        val profile = mockk<Profile>()
        val profileModel = mockk<ProfileModel> {
            every { copy() } returns this
        }

        every { profilePresenter.getProfile() } returns profile
        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(profile)
                )
            }
        }
    }

    @Test
    fun saveChanges_success() = runBlockingTest {
        val profile = PROFILE
        val profileModel = mockk<ProfileModel>()
        val updatedProfileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfile() } returns profile
        every { profilePresenter.getProfileModel() } returns profileModel

        every {
            profilePresenter.updateProfile(any())
        } returns UpdateProfileModel.Success(updatedProfileModel)

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _, queuedEventsObserver ->
                startEditing()
                setFullName("John Smith")
                saveChanges()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(profile),
                    Editing(profile, profile.copy(fullName = "John Smith")),
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
        val errorMessage = "abc"

        every { profilePresenter.getProfile() } returns PROFILE
        every { profilePresenter.getProfileModel() } returns mockk()

        every { profilePresenter.updateProfile(any()) } returns UpdateProfileModel.Error(errorMessage)

        with(newTestSubject()) {
            observeStateAndEvents { _, _, queuedEventsObserver ->
                startEditing()
                setFullName("John Smith")
                saveChanges()

                queuedEventsObserver.assertObserved(
                    NotifySavingChangesFailedEvent(errorMessage)
                )
            }
        }
    }

    @Test
    fun `Remain in editing state when saving changes fails`() = runBlockingTest {
        val profile = PROFILE
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfile() } returns profile
        every { profilePresenter.getProfileModel() } returns profileModel

        every { profilePresenter.updateProfile(any()) } returns UpdateProfileModel.Error("")

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()
                setFullName("John Smith")
                saveChanges()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(profile),
                    Editing(profile, profile.copy(fullName = "John Smith")),
                )
            }
        }
    }

    @Test
    fun `Saving is possible after correcting validation errors`() = runBlockingTest {
        val profile = PROFILE
        val profileModel = mockk<ProfileModel>()
        val updatedProfileModel = mockk<ProfileModel>()
        val errors = mockk<ValidationErrors>()

        every { profilePresenter.getProfile() } returns profile
        every { profilePresenter.getProfileModel() } returns profileModel

        every { profilePresenter.updateProfile(any()) } returnsMany listOf(
            UpdateProfileModel.ValidationError(errors),
            UpdateProfileModel.Success(updatedProfileModel)
        )

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()
                setFullName("   ")
                saveChanges()
                setFullName("John Smith")
                saveChanges()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(profile),
                    Editing(profile, profile.copy(fullName = "   ")),
                    Editing(profile, profile.copy(fullName = "   "), errors),
                    Editing(profile, profile.copy(fullName = "John Smith"), errors),
                    Viewing(updatedProfileModel),
                )
            }
        }
    }

    @Test
    fun `Saving changes produces errors when validations fail`() = runBlockingTest {
        val profile = PROFILE
        val profileModel = mockk<ProfileModel>()
        val errors = mockk<ValidationErrors>()

        every { profilePresenter.getProfile() } returns profile
        every { profilePresenter.getProfileModel() } returns profileModel

        every { profilePresenter.updateProfile(any()) } returns UpdateProfileModel.ValidationError(errors)

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()
                setFullName("   ")
                saveChanges()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(profile),
                    Editing(profile, profile.copy(fullName = "   ")),
                    Editing(profile, profile.copy(fullName = "   "), errors),
                )
            }
        }
    }

    @Test
    fun confirmDiscardChanges() = runBlockingTest {
        val profile = mockk<Profile>()
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfile() } returns profile
        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, eventsObserver ->
                startEditing()
                confirmDiscardChanges()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(profile),
                    Viewing(profileModel)
                )

                eventsObserver.assertObserved(NotifyNoChangesPerformedEvent)
            }
        }
    }

    @Test
    fun `Navigating back while viewing exits screen`() = runBlockingTest {
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, eventsObserver ->
                navigateBack()

                stateObserver.assertObserved(
                    Viewing(profileModel)
                )

                eventsObserver.assertObserved(ExitScreenEvent)
            }
        }
    }

    @Test
    fun `Navigating back without making changes returns to viewing`() = runBlockingTest {
        val profile = PROFILE
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfile() } returns profile
        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { stateObserver, _ ->
                startEditing()
                navigateBack()

                stateObserver.assertObserved(
                    Viewing(profileModel),
                    Editing(profile),
                    Viewing(profileModel),
                )
            }
        }
    }

    @Test
    fun `Navigating back without making changes notifies no changes performed`() = runBlockingTest {
        val profile = PROFILE
        val profileModel = mockk<ProfileModel>()

        every { profilePresenter.getProfile() } returns profile
        every { profilePresenter.getProfileModel() } returns profileModel

        with(newTestSubject()) {
            observeStateAndEvents { _, eventsObserver ->
                startEditing()
                navigateBack()

                eventsObserver.assertObserved(NotifyNoChangesPerformedEvent)
            }
        }
    }

    @Test
    fun `Navigating back with changes asks for confirmation to discard them`() = runBlockingTest {
        every { profilePresenter.getProfile() } returns PROFILE
        every { profilePresenter.getProfileModel() } returns mockk()

        with(newTestSubject()) {
            observeStateAndEvents { _, eventsObserver ->
                startEditing()
                setFullName("John Smith")
                navigateBack()

                eventsObserver.assertObserved(ConfirmDiscardChangesEvent)
            }
        }
    }

}
