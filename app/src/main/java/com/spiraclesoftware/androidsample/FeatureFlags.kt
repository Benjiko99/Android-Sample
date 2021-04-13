package com.spiraclesoftware.androidsample

object FeatureFlags {

    val PROFILE_FEATURE = object : FeatureFlag(isEnabled = true) {}

}

abstract class FeatureFlag(
    val isEnabled: Boolean = false
)