package com.spiraclesoftware.androidsample.feature.text_input

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.spiraclesoftware.androidsample.feature.text_input.TextInputViewModel.SendResultToCallerAndExitEvent
import com.spiraclesoftware.androidsample.feature.text_input.TextInputViewState.InputEntry
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
            vm.setInput("Hello World")
            vm.saveInput()

            stateObserver.assertObserved(
                InputEntry("Initial Value"),
                InputEntry("Hello World")
            )

            eventsObserver.assertObserved(
                SendResultToCallerAndExitEvent(REQUEST_KEY, "Hello World")
            )
        }
    }

}
