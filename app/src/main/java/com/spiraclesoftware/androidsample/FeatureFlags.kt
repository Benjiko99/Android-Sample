package com.spiraclesoftware.androidsample

object FeatureFlags {

    val PROFILE_FEATURE = object : FeatureFlag(isEnabled = false) {}

}

abstract class FeatureFlag(
    val isEnabled: Boolean = false
)