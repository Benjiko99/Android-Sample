object PluginVersion {
    const val android_gradle = Version.android_gradle
    const val google_services = Version.google_services
    const val kotlin = Version.kotlin
    const val kotlinter = Version.kotlinter
    const val safe_args = Version.navigation
    const val exhaustive = Version.exhaustive
}

object Plugin {

    const val android_gradle =
        "com.android.tools.build:gradle:${PluginVersion.android_gradle}"

    const val google_services =
        "com.google.gms:google-services:${PluginVersion.google_services}"

    const val kotlin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginVersion.kotlin}"

    const val kotlinter =
        "org.jmailen.gradle:kotlinter-gradle:${PluginVersion.kotlinter}"

    const val safeArgs =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${PluginVersion.safe_args}"

    const val exhaustive =
        "app.cash.exhaustive:exhaustive-gradle:${PluginVersion.exhaustive}"

}