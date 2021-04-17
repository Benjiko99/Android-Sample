package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.QueuedOneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.ProfileUpdateData
import com.spiraclesoftware.androidsample.feature.profile.ProfilePresenter.ProfileUpdate
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

    object ExitScreenEvent : OneShotEvent

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

        val data = ProfileUpdateData(fullName, dateOfBirth, phoneNumber, email)
        val result = presenter.updateProfile(data)

        when (result) {
            is ProfileUpdate.Success -> {
                viewState = Viewing(result.updatedProfileModel)
                postQueuedEvent(NotifyChangesSavedEvent)
            }
            is ProfileUpdate.ValidationError -> {
                viewState = Editing(result.validationErrors)
            }
            is ProfileUpdate.Error -> {
                viewState = Editing(validationErrors = null)
                postQueuedEvent(NotifySavingChangesFailedEvent(result.errorMessage))
            }
        }
    }

    fun confirmDiscardChanges() {
        if (viewState !is Editing) return

        viewState = Viewing(presenter.getProfileModel())
        postEvent(ExitScreenEvent)
    }

    fun exitScreen() {
        when (viewState) {
            is Viewing -> postEvent(ExitScreenEvent)
            is Editing -> postEvent(ConfirmDiscardChangesEvent)
        }
    }

}