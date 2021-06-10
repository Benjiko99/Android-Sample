plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}
apply(plugin = "org.jmailen.kotlinter")

android {
    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        compileSdkVersion(AppConfig.compileSdkVersion)
        buildToolsVersion(AppConfig.buildToolsVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation(project(":domain"))

    coreLibraryDesugaring(Dependency.core_library_desugaring)
    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.koin_core)

    kapt(Dependency.moshi_codegen)
    implementation(Dependency.moshi)
    implementation(Dependency.okhttp)
    implementation(Dependency.okhttp_logging)
    implementation(Dependency.retrofit)
    implementation(Dependency.retrofit_moshi)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.truth)
    testImplementation(Dependency.mockk)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.coroutines_test)
}