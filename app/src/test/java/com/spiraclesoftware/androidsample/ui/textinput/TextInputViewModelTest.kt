package com.spiraclesoftware.androidsample.ui.textinput

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.ui.textinput.TextInputViewModel.SendResultToCallerAndExitEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TextInputViewModelTest : ViewModelTest() {

    companion object {
        private const val REQUEST_KEY = "requestKey"
    }

    @Test
    fun `Validate then send input to caller`() = runBlockingTest {
        val vm = TextInputViewModel(TextInputType.NOTE, REQUEST_KEY, "Initial Value")

        vm.observeStateAndEvents { stateObserver, eventsObserver ->
            vm.validateThenSendInputToCaller("Hello World")

            stateObserver.assertObserved(
                Content(
                    "Initial Value",
                    null
                ),
                Content(
                    "Hello World",
                    null
                )
            )

            eventsObserver.assertObserved(
                SendResultToCallerAndExitEvent(REQUEST_KEY, "Hello World")
            )
        }
    }

}
