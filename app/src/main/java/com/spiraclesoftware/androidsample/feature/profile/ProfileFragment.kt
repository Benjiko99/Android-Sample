package com.spiraclesoftware.androidsample.feature.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.spiraclesoftware.androidsample.databinding.ProfileFragmentBinding
import com.spiraclesoftware.androidsample.framework.StandardFragment
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState as ViewState

class ProfileFragment :
    StandardFragment<ProfileFragmentBinding, ViewState, ProfileViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ProfileFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel() = getViewModelFromFactory()

    override fun render(viewState: ViewState) {
    }

}
