package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Editing
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Viewing

class ProfileViewModel(
    private val presenter: ProfilePresenter
) : RainbowCakeViewModel<ProfileViewState>(
    Viewing(presenter.getProfileModel())
) {

    fun enterEditing() {
        viewState = Editing(presenter.getProfileModel().copy())
    }

    fun saveChanges(fullName: String) {
        // update profileModel with user's inputs
        viewState = (viewState as Editing).copy(
            profileModel = presenter.getProfileModel().copy(
                fullName = fullName
            )
        )

        viewState = Viewing(presenter.getProfileModel())
    }

}