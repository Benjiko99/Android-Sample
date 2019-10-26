plugins {
    id("com.android.library")
    id("kotlin-allopen")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
}
apply(plugin = "org.jmailen.kotlinter")

android {
    compileSdkVersion(AppDeps.compileSdk)

    defaultConfig {
        minSdkVersion(AppDeps.minSdk)
        targetSdkVersion(AppDeps.targetSdk)
        versionCode = AppDeps.versionCode
        versionName = AppDeps.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = AppDeps.sourceCompat
        targetCompatibility = AppDeps.targetCompat
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

allOpen {
    // Makes annotated classes `open` in debug builds, used for mocking in unit tests.
    annotation("com.spiraclesoftware.core.testing.OpenClass")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(kotlin("stdlib-jdk8", Deps.kotlin))

    debugApi("com.squareup.leakcanary:leakcanary-android:${Deps.leakCanary}")

    api("com.jakewharton.timber:timber:${Deps.timber}")
    api("com.jakewharton.threetenabp:threetenabp:${Deps.threetenabp}")
    api("com.jakewharton:process-phoenix:${Deps.processPhoenix}")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutines}")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Deps.coroutines}")

    api("org.koin:koin-android:${Deps.koin}")
    api("org.koin:koin-androidx-viewmodel:${Deps.koin}")

    api("com.squareup.okhttp3:okhttp:${Deps.okhttp}")
    api("com.squareup.okhttp3:logging-interceptor:${Deps.okhttp}")

    api("com.squareup.retrofit2:retrofit:${Deps.retrofit}")
    api("com.squareup.retrofit2:converter-gson:${Deps.retrofit}")

    api("androidx.appcompat:appcompat:${Deps.androidx}")
    api("androidx.recyclerview:recyclerview:${Deps.androidx}")

    api("com.google.android.material:material:${Deps.androidx}")
}
