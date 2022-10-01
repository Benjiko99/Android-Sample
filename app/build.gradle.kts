plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs.kotlin")
}
apply(plugin = "org.jmailen.kotlinter")
apply(plugin = "app.cash.exhaustive")

android {
    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        compileSdk = AppConfig.compileSdkVersion
        buildToolsVersion = AppConfig.buildToolsVersion
        applicationId = AppConfig.id
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildTypes {
        named(BuildType.debug) {
            isMinifyEnabled = false
        }
        named(BuildType.release) {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":domain"))
    implementation(project(":data-local"))
    implementation(project(":data-remote"))

    coreLibraryDesugaring(Dependency.core_library_desugaring)
    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.coroutines_android)
    implementation(Dependency.rainbow_cake_core)
    implementation(Dependency.rainbow_cake_koin)
    implementation(Dependency.rainbow_cake_timber)
    implementation(Dependency.koin_android)
    implementation(Dependency.process_phoenix)
    implementation(Dependency.livedata_ktx)
    implementation(Dependency.navigation_ui_ktx)
    implementation(Dependency.navigation_fragment_ktx)
    implementation(Dependency.compose)
    implementation(Dependency.compose)
    implementation(Dependency.compose_tooling)
    implementation(Dependency.compose_runtime)
    implementation(Dependency.compose_livedata)
    implementation(Dependency.compose_foundation)
    implementation(Dependency.compose_layout)
    implementation(Dependency.compose_animation)
    implementation(Dependency.compose_activity)
    implementation(Dependency.compose_viewmodel)
    implementation(Dependency.compose_material)
    implementation(Dependency.compose_material_theme_adapter)
    implementation(Dependency.compose_material_icons)
    implementation(Dependency.compose_material_icons_extended)
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

    implementation(Dependency.requirektx_bundle)
    implementation(Dependency.requirektx_intent)
    implementation(Dependency.requirektx_work)

    implementation(platform(Dependency.firebase_bom))
    implementation(Dependency.firebase_crashlytics)
    implementation(Dependency.firebase_messaging)

    implementation(Dependency.leak_canary_plumber)
    debugImplementation(Dependency.leak_canary)

    debugImplementation(Dependency.beagle_drawer)
    debugImplementation(Dependency.beagle_log)
    releaseImplementation(Dependency.beagle_noop)
    releaseImplementation(Dependency.beagle_log_noop)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.truth)
    testImplementation(Dependency.mockk)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.fragment_test)
    testImplementation(Dependency.coroutines_test)
    testImplementation(Dependency.rainbow_cake_test)

    androidTestImplementation(Dependency.android_test_runner)
    androidTestImplementation(Dependency.android_test_junit)
    androidTestImplementation(Dependency.arch_test)
    androidTestImplementation(Dependency.junit)
    androidTestImplementation(Dependency.truth)
    androidTestImplementation(Dependency.koin_test)
}
