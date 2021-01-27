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
        sourceCompatibility = AppConfig.jvmTarget
        targetCompatibility = AppConfig.jvmTarget
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.koin_core)
    implementation(Dependency.threetenabp)

    kapt(Dependency.moshi_codegen)
    implementation(Dependency.moshi)
    implementation(Dependency.okhttp)
    implementation(Dependency.okhttp_logging)
    implementation(Dependency.retrofit)
    implementation(Dependency.retrofit_moshi)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.coroutines_test)
    testImplementation(Dependency.mockito_inline)
    testImplementation(Dependency.mockito_kotlin)
}