plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8", Dependencies.kotlin))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.coroutines}")

    implementation("org.koin:koin-core:${Dependencies.koin}")

    implementation("com.jakewharton.threetenabp:threetenabp:${Dependencies.threetenabp}")

    implementation("com.squareup.moshi:moshi:${Dependencies.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.moshi}")

    implementation("com.squareup.okhttp3:okhttp:${Dependencies.okhttp}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Dependencies.okhttp}")

    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofit}")
    implementation("com.squareup.retrofit2:converter-moshi:${Dependencies.retrofit}")

    testImplementation("junit:junit:${Dependencies.junit}")
    testImplementation("org.koin:koin-test:${Dependencies.koin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.coroutines}")
    testImplementation("org.mockito:mockito-inline:${Dependencies.mockito}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependencies.mockitoKotlin}")
}