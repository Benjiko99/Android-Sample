plugins {
    id("com.android.application")
    id("kotlin-allopen")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
}
apply(plugin = "org.jmailen.kotlinter")
apply(plugin = "app.cash.exhaustive")

android {
    compileSdkVersion(Application.compileSdk)

    defaultConfig {
        applicationId = Application.id
        minSdkVersion(Application.minSdk)
        targetSdkVersion(Application.targetSdk)
        versionCode = Application.versionCode
        versionName = Application.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
        sourceCompatibility = Application.sourceCompat
        targetCompatibility = Application.targetCompat
    }

    kotlinOptions {
        jvmTarget = Application.targetCompat.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
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
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":cache"))
    implementation(project(":local"))
    implementation(project(":remote"))

    implementation("co.zsmb:rainbow-cake-core:${Dependencies.rainbowCake}")
    implementation("co.zsmb:rainbow-cake-koin:${Dependencies.rainbowCake}")
    implementation("co.zsmb:rainbow-cake-timber:${Dependencies.rainbowCake}")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependencies.leakCanary}")
    implementation("com.facebook.stetho:stetho:${Dependencies.stetho}")

    implementation("com.jakewharton.threetenabp:threetenabp:${Dependencies.threetenabp}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Dependencies.coroutines}")

    implementation("org.koin:koin-android:${Dependencies.koin}")
    implementation("org.koin:koin-androidx-viewmodel:${Dependencies.koin}")

    implementation("androidx.navigation:navigation-fragment-ktx:${Dependencies.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Dependencies.navigation}")

    implementation("androidx.fragment:fragment-ktx:${Dependencies.androidxFragment}")
    implementation("androidx.recyclerview:recyclerview:${Dependencies.androidx}")
    implementation("androidx.constraintlayout:constraintlayout:${Dependencies.constraintLayout}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Dependencies.swipeRefreshLayout}")

    implementation("com.mikepenz:fastadapter:${Dependencies.fastAdapter}")
    implementation("com.mikepenz:fastadapter-extensions-binding:${Dependencies.fastAdapter}")
    implementation("com.mikepenz:fastadapter-extensions-diff:${Dependencies.fastAdapter}")
    implementation("io.cabriole:decorator:${Dependencies.decorator}")

    implementation("io.coil-kt:coil:${Dependencies.coil}")
    implementation("com.github.stfalcon:stfalcon-imageviewer:${Dependencies.imageViewer}")

    testImplementation("junit:junit:${Dependencies.junit}")
    testImplementation("androidx.arch.core:core-testing:${Dependencies.lifecycle}")
    testImplementation("androidx.fragment:fragment-testing:${Dependencies.androidxFragment}")
    testImplementation("co.zsmb:rainbow-cake-test:${Dependencies.rainbowCake}")
    testImplementation("org.koin:koin-test:${Dependencies.koin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.coroutines}")
    testImplementation("org.mockito:mockito-inline:${Dependencies.mockito}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependencies.mockitoKotlin}")
    testImplementation("org.threeten:threetenbp:${Dependencies.threetenabp}") {
        exclude("com.jakewharton.threetenabp", "threetenabp")
    }
    androidTestImplementation("androidx.test:runner:${Dependencies.androidxTestRunner}")
    androidTestImplementation("androidx.test.ext:junit:${Dependencies.androidxTestJUnit}")
    androidTestImplementation("org.mockito:mockito-android:${Dependencies.mockito}")
}
