plugins {
    id("com.android.library")
    kotlin("android")
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
    implementation(project(":domain"))
    api(project(":data-local"))
    api(project(":data-remote"))

    implementation(kotlin("stdlib-jdk8", Dependencies.kotlin))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.coroutines}")

    implementation("org.koin:koin-core:${Dependencies.koin}")
    implementation("com.jakewharton.threetenabp:threetenabp:${Dependencies.threetenabp}")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Dependencies.lifecycle}")

    testImplementation("junit:junit:${Dependencies.junit}")
    testImplementation("org.koin:koin-test:${Dependencies.koin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.coroutines}")
    testImplementation("org.mockito:mockito-inline:${Dependencies.mockito}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependencies.mockitoKotlin}")
    testImplementation("androidx.arch.core:core-testing:${Dependencies.arch}")
}