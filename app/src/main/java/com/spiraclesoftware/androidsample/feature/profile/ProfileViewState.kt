package com.spiraclesoftware.androidsample.feature.profile

sealed class ProfileViewState {

    object Initial : ProfileViewState()

    data class Content(val profileModel: ProfileModel) : ProfileViewState()

    data class Error(val message: String?) : ProfileViewState()

}
