package com.spiraclesoftware.androidsample.feature

object FeatureFlags {

    val PROFILE_FEATURE = object : FeatureFlag(isEnabled = true) {}

}

abstract class FeatureFlag(
    val isEnabled: Boolean = false
)