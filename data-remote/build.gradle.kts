plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}
apply(plugin = "org.jmailen.kotlinter")

android {
    namespace = "com.spiraclesoftware.androidsample.data_remote"

    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        compileSdk = AppConfig.compileSdkVersion
        buildToolsVersion = AppConfig.buildToolsVersion
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        buildConfig = true
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

    debugApi(Dependency.beagle_okhttp)
    releaseApi(Dependency.beagle_okhttp_noop)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.truth)
    testImplementation(Dependency.mockk)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.coroutines_test)
}