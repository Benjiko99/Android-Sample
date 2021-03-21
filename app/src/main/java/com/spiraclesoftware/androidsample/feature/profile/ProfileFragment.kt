package com.spiraclesoftware.androidsample.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.spiraclesoftware.androidsample.databinding.ProfileFragmentBinding
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Content
import com.spiraclesoftware.androidsample.framework.StandardFragment
import com.spiraclesoftware.androidsample.util.DelightUI
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState as ViewState

class ProfileFragment :
    StandardFragment<ProfileFragmentBinding, ViewState, ProfileViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ProfileFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel() = getViewModelFromFactory()

    override fun render(viewState: ViewState) {
        when (viewState) {
            is Content -> {
                with(binding) {
                    with(viewState.profileModel) {
                        fullNameView.text = fullName
                        dateOfBirthView.text = dateOfBirth
                        phoneNumberView.text = phoneNumber
                        emailView.text = email
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())

        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView) {
            headerView.height
        }
    }

}
