plugins {
    id("com.android.library")
    kotlin("android")
}
apply(plugin = "org.jmailen.kotlinter")

android {
    compileSdkVersion(AppConfig.compileSdkVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    compileOptions {
        sourceCompatibility = AppConfig.jvmTarget
        targetCompatibility = AppConfig.jvmTarget
    }

    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget.toString()
    }
}

dependencies {
    implementation(project(":domain"))
    api(project(":data-local"))
    api(project(":data-remote"))

    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.koin_core)
    implementation(Dependency.threetenabp)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.arch_test)
    testImplementation(Dependency.coroutines_test)
    testImplementation(Dependency.mockito_inline)
    testImplementation(Dependency.mockito_kotlin)
    testImplementation("org.threeten:threetenbp:${Version.threetenabp}") {
        exclude("com.jakewharton.threetenabp", "threetenabp")
    }
}