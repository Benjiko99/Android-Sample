package com.spiraclesoftware.androidsample.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.ProfileFragmentBinding
import com.spiraclesoftware.androidsample.extension.getText
import com.spiraclesoftware.androidsample.extension.onDoneAction
import com.spiraclesoftware.androidsample.extension.setText
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewModel.ConfirmDiscardChangesEvent
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewModel.ExitEvent
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

    private fun onEditClicked() {
        viewModel.startEditing()
    }

    private fun onSaveClicked() = with (binding) {
        viewModel.saveChanges(
            fullName = fullNameView.getText().toString()
        )
    }

    private fun onDiscardChangesConfirmed() {
        viewModel.confirmDiscardChanges()
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                onEditClicked()
                return true
            }
            R.id.action_save -> {
                onSaveClicked()
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

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is ConfirmDiscardChangesEvent ->
                showDiscardChangesDialog()
            is ExitEvent ->
                findNavController().navigateUp()
        }
    }

    private fun showDiscardChangesDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.discard_changes_dialog__message)
            .setNegativeButton(R.string.discard_changes_dialog__cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.discard_changes_dialog__discard) { _, _ ->
                onDiscardChangesConfirmed()
            }.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.exitScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupEditorActions()
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())

        toolbar.setNavigationOnClickListener {
            viewModel.exitScreen()
        }

        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView) {
            headerView.height
        }

        toolbar.inflateMenu(R.menu.profile_menu)
        toolbar.setOnMenuItemClickListener(::onMenuItemClicked)
    }

    private fun setupEditorActions() {
        binding.contentView.children
            .filterIsInstance<TextInputLayout>()
            .map { it.editText!! }
            .forEach { it.onDoneAction(::onSaveClicked) }
    }

}
