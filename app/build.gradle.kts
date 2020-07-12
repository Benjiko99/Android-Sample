plugins {
    id("com.android.application")
    id("kotlin-allopen")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
    id("androidx.navigation.safeargs.kotlin")
}
apply(plugin = "org.jmailen.kotlinter")

android {
    compileSdkVersion(AppDeps.compileSdk)

    defaultConfig {
        applicationId = AppDeps.id
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
    implementation(project(":core"))

    implementation("com.facebook.stetho:stetho:${Deps.stetho}")

    implementation("androidx.navigation:navigation-fragment-ktx:${Deps.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Deps.navigation}")

    implementation("androidx.constraintlayout:constraintlayout:${Deps.constraintLayout}")

    implementation("com.mikepenz:fastadapter:${Deps.fastAdapter}")
    implementation("com.mikepenz:fastadapter-extensions-binding:${Deps.fastAdapter}")

    implementation("io.cabriole:decorator:${Deps.decorator}")

    testImplementation("junit:junit:${Deps.junit}")
    testImplementation("androidx.arch.core:core-testing:${Deps.lifecycle}")

    testImplementation("co.zsmb:rainbow-cake-test:${Deps.rainbowCake}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Deps.coroutines}")
    testImplementation("org.mockito:mockito-inline:${Deps.mockito}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Deps.mockitoKotlin}")
    androidTestImplementation("org.mockito:mockito-android:${Deps.mockito}")
}
