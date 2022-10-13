package com.spiraclesoftware.androidsample.feature

object FeatureFlags {

    val PROFILE_FEATURE = object : FeatureFlag(isEnabled = true) {}

    val SETTINGS_FEATURE = object : FeatureFlag(isEnabled = false) {}

}

abstract class FeatureFlag(
    val isEnabled: Boolean = false
) {

    val isDisabled: Boolean
        get() = !isEnabled

}