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
        sourceCompatibility = AppConfig.jvmTarget
        targetCompatibility = AppConfig.jvmTarget
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.serialization_json)
    implementation(Dependency.koin_android)
    implementation(Dependency.threetenabp)

    kapt(Dependency.room_compiler)
    implementation(Dependency.room_runtime)
    implementation(Dependency.room_ktx)
    testImplementation(Dependency.room_testing)
    androidTestImplementation(Dependency.room_android_testing)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.coroutines_test)
    testImplementation(Dependency.mockito_inline)
    testImplementation(Dependency.mockito_kotlin)
    testImplementation("org.threeten:threetenbp:${Version.threetenabp}") {
        exclude("com.jakewharton.threetenabp", "threetenabp")
    }

    androidTestImplementation(Dependency.android_test_runner)
    androidTestImplementation(Dependency.android_test_junit)
    androidTestImplementation(Dependency.mockito_android)
}