plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}
apply(plugin = "org.jmailen.kotlinter")
apply(plugin = "app.cash.exhaustive")

android {
    compileSdkVersion(AppConfig.compileSdkVersion)

    defaultConfig {
        applicationId = AppConfig.id
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        buildToolsVersion(AppConfig.buildToolsVersion)
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
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    buildTypes {
        named(BuildType.release) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.coroutines_android)
    implementation(Dependency.rainbowcake_core)
    implementation(Dependency.rainbowcake_koin)
    implementation(Dependency.rainbowcake_timber)
    implementation(Dependency.koin_android)
    implementation(Dependency.koin_viewmodel)
    implementation(Dependency.threetenabp)
    implementation(Dependency.stetho)
    implementation(Dependency.process_phoenix)
    implementation(Dependency.livedata_ktx)
    implementation(Dependency.navigation_ui_ktx)
    implementation(Dependency.navigation_fragment_ktx)
    implementation(Dependency.coil)
    implementation(Dependency.imageviewer)
    implementation(Dependency.material)
    implementation(Dependency.appcompat)
    implementation(Dependency.fragment_ktx)
    implementation(Dependency.recycler_view)
    implementation(Dependency.constraint_layout)
    implementation(Dependency.swipe_refresh_layout)
    implementation(Dependency.fastadapter)
    implementation(Dependency.fastadapter_binding)
    implementation(Dependency.fastadapter_diff)
    implementation(Dependency.decorator)

    debugImplementation(Dependency.leak_canary)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.truth)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.fragment_test)
    testImplementation(Dependency.coroutines_test)
    testImplementation(Dependency.rainbowcake_test)
    testImplementation(Dependency.mockito_inline)
    testImplementation(Dependency.mockito_kotlin)
    testImplementation("org.threeten:threetenbp:${Version.threetenabp}") {
        exclude("com.jakewharton.threetenabp", "threetenabp")
    }

    androidTestImplementation(Dependency.android_test_runner)
    androidTestImplementation(Dependency.android_test_junit)
    androidTestImplementation(Dependency.mockito_android)
}
