package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Editing
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Viewing

class ProfileViewModel(
    private val presenter: ProfilePresenter
) : RainbowCakeViewModel<ProfileViewState>(
    Viewing(presenter.getProfileModel())
) {

    object ConfirmDiscardChangesEvent: OneShotEvent

    object ExitEvent: OneShotEvent

    fun startEditing() {
        if (viewState !is Viewing) return

        viewState = Editing(presenter.getProfileModel().copy())
    }

    fun saveChanges(fullName: String) {
        if (viewState !is Editing) return

        // update profileModel with user's changes
//        viewState = (viewState as Editing).copy(
//            profileModel = presenter.getProfileModel().copy(
//                fullName = fullName
//            )
//        )

        viewState = Viewing(presenter.getProfileModel())
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