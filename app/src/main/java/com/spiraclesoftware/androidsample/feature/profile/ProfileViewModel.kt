package com.spiraclesoftware.androidsample.feature.profile

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Content
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Initial

class ProfileViewModel(
    private val presenter: ProfilePresenter
) : RainbowCakeViewModel<ProfileViewState>(Initial) {

    init {
        viewState = Content(
            profileModel = presenter.getProfileModel()
        )
    }

}