package com.spiraclesoftware.androidsample.feature.profile

sealed class ProfileViewState {

    data class Viewing(val profileModel: ProfileModel) : ProfileViewState()

    data class Editing(val profileModel: ProfileModel) : ProfileViewState()

}
