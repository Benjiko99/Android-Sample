package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.QueuedOneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.feature.profile.ProfilePresenter.UpdateProfileModel
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.*
import java.time.LocalDate

class ProfileViewModel(
    private val presenter: ProfilePresenter
) : RainbowCakeViewModel<ProfileViewState>(
    Viewing(presenter.getProfileModel())
) {

    object NotifyChangesSavedEvent : QueuedOneShotEvent

    data class NotifySavingChangesFailedEvent(val message: String) : QueuedOneShotEvent

    object NotifyNoChangesPerformedEvent : QueuedOneShotEvent

    object ConfirmDiscardChangesEvent : OneShotEvent

    data class ShowDateOfBirthPickerEvent(val openAt: LocalDate) : OneShotEvent

    object ExitScreenEvent : OneShotEvent

    fun startEditing() {
        if (viewState !is Viewing) return

        viewState = Editing(presenter.getProfile())
    }

    fun saveChanges() = execute {
        if (viewState !is Editing) return@execute
        val editingState = (viewState as Editing)

        if (editingState.initialProfile == editingState.modifiedProfile) {
            viewState = Viewing(presenter.getProfileModel())
            postEvent(NotifyNoChangesPerformedEvent)
            return@execute
        }

        when (val result = presenter.updateProfile(editingState.modifiedProfile)) {
            is UpdateProfileModel.Success -> {
                viewState = Viewing(result.updatedProfileModel)
                postQueuedEvent(NotifyChangesSavedEvent)
            }
            is UpdateProfileModel.ValidationError -> {
                viewState = editingState.copy(validationErrors = result.validationErrors)
            }
            is UpdateProfileModel.Error -> {
                viewState = editingState.copy(validationErrors = null)
                postQueuedEvent(NotifySavingChangesFailedEvent(result.errorMessage))
            }
        }
    }

    fun confirmDiscardChanges() {
        if (viewState !is Editing) return

        viewState = Viewing(presenter.getProfileModel())
        postEvent(NotifyNoChangesPerformedEvent)
    }

    fun showDateOfBirthPicker() {
        if (viewState !is Editing) return

        val openAt = (viewState as Editing).modifiedProfile.dateOfBirth
        postEvent(ShowDateOfBirthPickerEvent(openAt))
    }

    fun navigateBack() {
        when (viewState) {
            is Viewing -> postEvent(ExitScreenEvent)
            is Editing -> {
                val editingState = (viewState as Editing)

                if (editingState.initialProfile == editingState.modifiedProfile) {
                    viewState = Viewing(presenter.getProfileModel())
                    postEvent(NotifyNoChangesPerformedEvent)
                } else {
                    postEvent(ConfirmDiscardChangesEvent)
                }
            }
        }
    }

    fun setFullName(fullName: String) {
        (viewState as? Editing)?.apply {
            viewState = copy(
                modifiedProfile = modifiedProfile.copy(fullName = fullName)
            )
        }
    }

    fun setDateOfBirth(dateOfBirth: LocalDate) {
        (viewState as? Editing)?.apply {
            viewState = copy(
                modifiedProfile = modifiedProfile.copy(dateOfBirth = dateOfBirth)
            )
        }
    }

    fun setPhoneNumber(phoneNumber: String) {
        (viewState as? Editing)?.apply {
            viewState = copy(
                modifiedProfile = modifiedProfile.copy(phoneNumber = phoneNumber)
            )
        }
    }

    fun setEmail(email: String) {
        (viewState as? Editing)?.apply {
            viewState = copy(
                modifiedProfile = modifiedProfile.copy(email = email)
            )
        }
    }

}