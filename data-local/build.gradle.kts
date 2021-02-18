plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version Version.kotlin
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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs(files("$projectDir/schemas"))
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = AppConfig.jvmTarget
        targetCompatibility = AppConfig.jvmTarget
    }
}

dependencies {
    implementation(project(":domain"))

    coreLibraryDesugaring(Dependency.core_library_desugaring)
    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.serialization_json)
    implementation(Dependency.koin_android)

    kapt(Dependency.room_compiler)
    implementation(Dependency.room_runtime)
    implementation(Dependency.room_ktx)
    testImplementation(Dependency.room_testing)
    androidTestImplementation(Dependency.room_android_testing)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.truth)
    testImplementation(Dependency.mockk)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.coroutines_test)

    androidTestImplementation(Dependency.android_test_runner)
    androidTestImplementation(Dependency.android_test_junit)
    androidTestImplementation(Dependency.truth)
}