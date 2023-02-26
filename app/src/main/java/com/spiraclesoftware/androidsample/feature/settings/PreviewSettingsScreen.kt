package com.spiraclesoftware.androidsample.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.themeadapter.material.MdcTheme
import com.spiraclesoftware.androidsample.R

@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    when (uiState) {
        is SettingsScreenState.Success -> {
            MdcTheme() {
                Column() {
                    TopAppBar() {
                        Text(text = stringResource(id = R.string.settings__title))
                    }
                    Text(text = "Hello World")
                }
            }
        }
    }
}