package com.spiraclesoftware.androidsample.feature.profile

sealed class ProfileViewState {

    object Initial : ProfileViewState()

    data class Error(val message: String?) : ProfileViewState()

}
