object PluginVersion {
    const val androidGradle = Version.android_gradle
    const val kotlin = Version.kotlin
    const val kotlinter = Version.kotlinter
    const val safeArgs = Version.navigation
    const val exhaustive = Version.exhaustive
}

object Plugin {
    const val androidGradle = "com.android.tools.build:gradle:${PluginVersion.androidGradle}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersion.kotlin}"
    const val kotlinter = "org.jmailen.gradle:kotlinter-gradle:${PluginVersion.kotlinter}"
    const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${PluginVersion.safeArgs}"
    const val exhaustive = "app.cash.exhaustive:exhaustive-gradle:${PluginVersion.exhaustive}"
}