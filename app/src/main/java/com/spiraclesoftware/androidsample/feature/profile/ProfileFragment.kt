package com.spiraclesoftware.androidsample.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.ProfileFragmentBinding
import com.spiraclesoftware.androidsample.extension.getText
import com.spiraclesoftware.androidsample.extension.setText
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Editing
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Viewing
import com.spiraclesoftware.androidsample.framework.StandardFragment
import com.spiraclesoftware.androidsample.util.DelightUI
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState as ViewState

class ProfileFragment :
    StandardFragment<ProfileFragmentBinding, ViewState, ProfileViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ProfileFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel() = getViewModelFromFactory()

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                viewModel.enterEditing()
                return true
            }
            R.id.action_save -> {
                viewModel.saveChanges(
                    fullName = binding.fullNameView.getText().toString()
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun render(viewState: ViewState): Unit = with(binding) {
        toolbar.menu.findItem(R.id.action_edit).isVisible = viewState is Viewing
        toolbar.menu.findItem(R.id.action_save).isVisible = viewState is Editing

        binding.fullNameView.isEnabled = viewState is Editing
        binding.dateOfBirthView.isEnabled = viewState is Editing
        binding.phoneNumberView.isEnabled = viewState is Editing
        binding.emailView.isEnabled = viewState is Editing

        when (viewState) {
            is Viewing -> {
                headerView.requestFocus()
            }
        }

        val profileModel = when (viewState) {
            is Viewing -> viewState.profileModel
            is Editing -> viewState.profileModel
        }

        with(profileModel) {
            fullNameView.setText(fullName)
            dateOfBirthView.setText(dateOfBirth)
            phoneNumberView.setText(phoneNumber)
            emailView.setText(email)
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

        toolbar.inflateMenu(R.menu.profile_menu)
        toolbar.setOnMenuItemClickListener(::onMenuItemClicked)
    }

}
