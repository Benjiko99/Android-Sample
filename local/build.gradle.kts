plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version Dependencies.kotlin
}
apply(plugin = "org.jmailen.kotlinter")

android {
    compileSdkVersion(Application.compileSdk)

    defaultConfig {
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

    compileOptions {
        sourceCompatibility = Application.sourceCompat
        targetCompatibility = Application.targetCompat
    }

    kotlinOptions {
        jvmTarget = Application.targetCompat.toString()
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8", Dependencies.kotlin))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.coroutines}")

    implementation("org.koin:koin-android:${Dependencies.koin}")
    implementation("com.jakewharton.threetenabp:threetenabp:${Dependencies.threetenabp}")

    kapt("androidx.room:room-compiler:${Dependencies.room}")
    implementation("androidx.room:room-runtime:${Dependencies.room}")
    implementation("androidx.room:room-ktx:${Dependencies.room}")
    testImplementation("androidx.room:room-testing:${Dependencies.room}")
    androidTestImplementation("androidx.room:room-testing:${Dependencies.roomTest}")

    testImplementation("junit:junit:${Dependencies.junit}")
    testImplementation("org.koin:koin-test:${Dependencies.koin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.coroutines}")
    testImplementation("org.mockito:mockito-inline:${Dependencies.mockito}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependencies.mockitoKotlin}")

    androidTestImplementation("androidx.test:runner:${Dependencies.androidxTestRunner}")
    androidTestImplementation("androidx.test.ext:junit:${Dependencies.androidxTestJUnit}")
    androidTestImplementation("org.mockito:mockito-android:${Dependencies.mockito}")
}