package com.spiraclesoftware.androidsample.feature.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsScreenState.Success)
    val uiState: StateFlow<SettingsScreenState> = _uiState
}