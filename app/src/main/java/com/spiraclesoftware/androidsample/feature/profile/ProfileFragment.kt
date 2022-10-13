package com.spiraclesoftware.androidsample.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.spiraclesoftware.androidsample.view.TextInputLayout
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.ProfileFragmentBinding
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewModel.*
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Editing
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState.Viewing
import com.spiraclesoftware.androidsample.framework.core.StandardFragment
import com.spiraclesoftware.androidsample.framework.extensions.*
import com.spiraclesoftware.androidsample.framework.utils.DelightUI
import java.time.*
import java.time.format.DateTimeFormatter
import com.spiraclesoftware.androidsample.feature.profile.ProfileViewState as ViewState

class ProfileFragment :
    StandardFragment<ProfileFragmentBinding, ViewState, ProfileViewModel>() {

    override var themeResId: Int = R.style.AppTheme_Surface

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ProfileFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel() = getViewModelFromFactory()

    private fun onEditClicked() {
        viewModel.startEditing()
    }

    private fun onSaveClicked() {
        viewModel.saveChanges()
    }

    private fun onDiscardChangesConfirmed() {
        viewModel.confirmDiscardChanges()
    }

    private fun onDateOfBirthClicked() {
        viewModel.showDateOfBirthPicker()
    }

    private fun onDateOfBirthPicked(epochMillis: Long) {
        val localDate = Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.of("UTC"))
            .toLocalDate()

        viewModel.setDateOfBirth(localDate)
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

        if (viewState is Viewing) {
            // clears focus from fields
            headerView.requestFocus()
        }

        applyToMany(fullNameField, dateOfBirthField, phoneNumberField, emailField) {
            isEnabled = viewState is Editing
        }

        if (viewState is Viewing) {
            with(viewState.profileModel) {
                fullNameView.setText(fullName)
                dateOfBirthView.setText(dateOfBirth)
                phoneNumberView.setText(phoneNumber)
                emailView.setText(email)
            }
        }

        if (viewState is Editing) {
            with(viewState.modifiedProfile) {
                dateOfBirthView.setText(dateOfBirth.format(DateTimeFormatter.ISO_LOCAL_DATE))
            }
        }

        val errors = (viewState as? Editing)?.validationErrors
        fullNameField.showOrHideError(errors?.fullNameError)
        dateOfBirthField.showOrHideError(errors?.dateOfBirthError)
        phoneNumberField.showOrHideError(errors?.phoneNumberError)
        emailField.showOrHideError(errors?.emailError)
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NotifyChangesSavedEvent -> {
                hideSoftKeyboard()
                showSnackbar(R.string.profile__changes_saved, Snackbar.LENGTH_LONG)
            }
            is NotifySavingChangesFailedEvent ->
                showSnackbar(event.message, Snackbar.LENGTH_LONG)
            is NotifyNoChangesPerformedEvent ->
                showSnackbar(R.string.profile__no_changes_performed, Snackbar.LENGTH_LONG)
            is ConfirmDiscardChangesEvent ->
                showDiscardChangesDialog()
            is ShowDateOfBirthPickerEvent ->
                showDateOfBirthPicker(event.openAt)
            is ExitScreenEvent ->
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

    private fun showDateOfBirthPicker(openAt: LocalDate) {
        val initialSelection = LocalDateTime.of(openAt, LocalTime.MIDNIGHT)
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.profile__date_of_birth_picker_title)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setOpenAt(initialSelection)
                    .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            )
            .setSelection(initialSelection)
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            onDateOfBirthPicked(selection)
        }

        picker.show(parentFragmentManager, "dateOfBirth")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.navigateBack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupEditorActions()
        setupFieldsBinding()
        setupDateOfBirthPicker()
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())

        toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
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

    private fun setupFieldsBinding() = with(binding) {
        fullNameView.doAfterTextChanged {
            viewModel.setFullName(it!!.toString())
        }
        phoneNumberView.doAfterTextChanged {
            viewModel.setPhoneNumber(it!!.toString())
        }
        emailView.doAfterTextChanged {
            viewModel.setEmail(it!!.toString())
        }
    }

    private fun setupDateOfBirthPicker() {
        binding.dateOfBirthField.setupNonEditable {
            onDateOfBirthClicked()
        }
    }

}
