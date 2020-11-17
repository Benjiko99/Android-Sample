package com.spiraclesoftware.androidsample.ui.textinput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import com.spiraclesoftware.androidsample.StandardFragment
import com.spiraclesoftware.androidsample.databinding.TextInputFragmentBinding
import com.spiraclesoftware.androidsample.ui.textinput.TextInputFragment.Companion.RESULT_KEY
import com.spiraclesoftware.androidsample.ui.textinput.TextInputViewModel.SendResultToCallerAndExitEvent
import com.spiraclesoftware.core.extensions.onClick
import com.spiraclesoftware.core.extensions.onDoneAction
import com.spiraclesoftware.core.extensions.showSoftKeyboard
import com.spiraclesoftware.core.extensions.string
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Fragment that presents the user with a single text-field, and returns the user-provided value
 * back to the calling fragment.
 *
 * Used with screens that don't directly consist of editable fields, but still want to let the user
 * change them.
 *
 *
 * In the calling fragment's `onCreate()`, register a result listener with the [RESULT_KEY]:
 * ```
 * setFragmentResultListener(TEXT_INPUT_REQUEST_KEY) { key, bundle ->
 *     val result = bundle.getString(TextInputFragment.RESULT_BUNDLE_KEY)
 *     viewModel.onInputChanged(result)
 * }
 * ```
 */
class TextInputFragment :
    StandardFragment<TextInputFragmentBinding, TextInputViewState, TextInputViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        TextInputFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel(): TextInputViewModel =
        TextInputFragmentArgs.fromBundle(requireArguments()).let { args ->
            getViewModel {
                parametersOf(args.strategyType, args.requestKey, args.initialValue)
            }
        }

    companion object {
        const val RESULT_KEY = "textInputResult"
    }

    override fun render(viewState: TextInputViewState) = with(binding) {
        when (viewState) {
            is TextInputEntry -> {
                inputEditText.setText(viewState.input)
                inputEditText.setSelection(viewState.input.length)

                inputLayout.error = viewState.error?.formattedMessage(requireContext())
            }
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is SendResultToCallerAndExitEvent -> {
                setFragmentResult(event.requestKey, bundleOf(RESULT_KEY to event.result))
                findNavController().navigateUp()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setupWithNavController(findNavController())

        toolbar.title = string(viewModel.toolbarTitleRes)
        inputLayout.hint = string(viewModel.inputHintRes)

        saveButton.onClick(::onSaveClicked)
        inputEditText.onDoneAction(::onSaveClicked)

        showSoftKeyboard(inputEditText)
    }

    private fun onSaveClicked() = with(binding) {
        val input = inputEditText.text.toString()
        viewModel.validateThenSendInputToCaller(input)
    }

}
