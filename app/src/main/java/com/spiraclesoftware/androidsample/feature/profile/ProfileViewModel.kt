package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.QueuedOneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.feature.profile.ProfilePresenter.UpdateProfileResult
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Editing
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Viewing

class ProfileViewModel(
    private val presenter: ProfilePresenter
) : RainbowCakeViewModel<ProfileViewState>(
    Viewing(presenter.getProfileModel())
) {

    object NotifyChangesSavedEvent : QueuedOneShotEvent

    data class NotifySavingChangesFailedEvent(val message: String) : QueuedOneShotEvent

    object ConfirmDiscardChangesEvent : OneShotEvent

    object ExitEvent : OneShotEvent

    fun startEditing() {
        if (viewState !is Viewing) return

        viewState = Editing()
    }

    fun saveChanges(
        fullName: String,
        dateOfBirth: String,
        phoneNumber: String,
        email: String
    ) = execute {
        if (viewState !is Editing) return@execute

        val result = presenter.updateProfile(fullName, dateOfBirth, phoneNumber, email)

        when (result) {
            is UpdateProfileResult.Success -> {
                viewState = Viewing(result.updatedProfileModel)
                postQueuedEvent(NotifyChangesSavedEvent)
            }
            is UpdateProfileResult.ValidationError -> {
                viewState = Editing(result.validationErrors)
            }
            is UpdateProfileResult.Error -> {
                viewState = Editing(validationErrors = null)
                postQueuedEvent(NotifySavingChangesFailedEvent(result.errorMessage))
            }
        }
    }

    fun confirmDiscardChanges() {
        if (viewState !is Editing) return

        viewState = Viewing(presenter.getProfileModel())
        postEvent(ExitEvent)
    }

    fun exitScreen() {
        when (viewState) {
            is Viewing -> postEvent(ExitEvent)
            is Editing -> postEvent(ConfirmDiscardChangesEvent)
        }
    }

}