package com.spiraclesoftware.androidsample.feature.text_input

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val textInputModule = module {

    viewModel { (inputType: TextInputType, requestKey: String, initialInput: String) ->
        TextInputViewModel(inputType, requestKey, initialInput)
    }

}