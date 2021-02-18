object Version {
    // Plugins
    const val android_gradle = "4.1.2"
    const val kotlinter = "3.2.0"
    const val exhaustive = "0.1.1"

    // Core
    const val kotlin = "1.4.21"
    const val core_library_desugaring = "1.1.1"
    const val coroutines = "1.4.2"
    const val rainbow_cake = "1.2.0"
    const val koin = "2.2.0-rc-1"

    // Debugging
    const val leak_canary = "2.3"
    const val process_phoenix = "2.0.0"
    const val stetho = "1.5.1"

    // Networking
    const val okhttp = "4.7.2"
    const val retrofit = "2.9.0"
    const val moshi = "1.11.0"
    const val serialization_json = "1.0.1"

    // Database
    const val room = "2.2.5"
    const val room_test = "2.2.6"

    // User Interface
    const val androidx = "1.1.0"
    const val androidx_fragment = "1.3.0-beta01"
    const val arch = "2.1.0"
    const val lifecycle = "2.2.0"
    const val navigation = "2.2.2"
    const val coil = "1.0.0-rc3"
    const val fastadapter = "5.2.1"
    const val decorator = "1.2.0"
    const val imageviewer = "1.0.1"
    const val swipe_refresh_layout = "1.1.0"
    const val constraint_layout = "2.0.4"

    // Testing
    const val androidx_test = "1.3.0"
    const val androidx_test_junit = "1.1.2"
    const val junit = "4.13"
    const val truth = "1.1"
    const val mockk = "1.10.5"
}

object Dependency {

    //region Core
    const val kotlin =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin}"

    const val core_library_desugaring =
        "com.android.tools:desugar_jdk_libs:${Version.core_library_desugaring}"

    const val coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"

    const val coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"

    const val coroutines_test =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutines}"
    //endregion


    //region Rainbow Cake
    const val rainbowcake_core =
        "co.zsmb:rainbow-cake-core:${Version.rainbow_cake}"

    const val rainbowcake_koin =
        "co.zsmb:rainbow-cake-koin:${Version.rainbow_cake}"

    const val rainbowcake_timber =
        "co.zsmb:rainbow-cake-timber:${Version.rainbow_cake}"

    const val rainbowcake_test =
        "co.zsmb:rainbow-cake-test:${Version.rainbow_cake}"
    //endregion


    //region Koin
    const val koin_core =
        "org.koin:koin-core:${Version.koin}"

    const val koin_android =
        "org.koin:koin-android:${Version.koin}"

    const val koin_viewmodel =
        "org.koin:koin-androidx-viewmodel:${Version.koin}"

    const val koin_test =
        "org.koin:koin-test:${Version.koin}"
    //endregion


    //region Debugging
    const val leak_canary =
        "com.squareup.leakcanary:leakcanary-android:${Version.leak_canary}"

    const val stetho =
        "com.facebook.stetho:stetho:${Version.stetho}"

    const val process_phoenix =
        "com.jakewharton:process-phoenix:${Version.process_phoenix}"
    //endregion


    //region Lifecycle
    const val livedata_ktx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    //endregion


    //region Navigation
    const val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"

    const val navigation_ui_ktx =
        "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    //endregion


    //region Networking
    const val okhttp =
        "com.squareup.okhttp3:okhttp:${Version.okhttp}"

    const val okhttp_logging =
        "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"

    const val retrofit =
        "com.squareup.retrofit2:retrofit:${Version.retrofit}"

    const val retrofit_moshi =
        "com.squareup.retrofit2:converter-moshi:${Version.retrofit}"

    const val moshi =
        "com.squareup.moshi:moshi:${Version.moshi}"

    const val moshi_codegen =
        "com.squareup.moshi:moshi-kotlin-codegen:${Version.moshi}"

    const val serialization_json =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.serialization_json}"
    //endregion


    //region Database
    const val room_compiler =
        "androidx.room:room-compiler:${Version.room}"

    const val room_runtime =
        "androidx.room:room-runtime:${Version.room}"

    const val room_ktx =
        "androidx.room:room-ktx:${Version.room}"

    const val room_testing =
        "androidx.room:room-testing:${Version.room}"

    const val room_android_testing =
        "androidx.room:room-testing:${Version.room_test}"
    //endregion


    //region User Interface
    const val coil =
        "io.coil-kt:coil:${Version.coil}"

    const val imageviewer =
        "com.github.stfalcon:stfalcon-imageviewer:${Version.imageviewer}"

    const val material =
        "com.google.android.material:material:${Version.androidx}"

    const val appcompat =
        "androidx.appcompat:appcompat:${Version.androidx}"

    const val fragment_ktx =
        "androidx.fragment:fragment-ktx:${Version.androidx_fragment}"

    const val fragment_test =
        "androidx.fragment:fragment-testing:${Version.androidx_fragment}"

    const val recycler_view =
        "androidx.recyclerview:recyclerview:${Version.androidx}"

    const val constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Version.constraint_layout}"

    const val swipe_refresh_layout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Version.swipe_refresh_layout}"

    const val fastadapter =
        "com.mikepenz:fastadapter:${Version.fastadapter}"

    const val fastadapter_binding =
        "com.mikepenz:fastadapter-extensions-binding:${Version.fastadapter}"

    const val fastadapter_diff =
        "com.mikepenz:fastadapter-extensions-diff:${Version.fastadapter}"

    const val decorator =
        "io.cabriole:decorator:${Version.decorator}"
    //endregion


    //region Testing
    const val android_test_runner =
        "androidx.test:runner:${Version.androidx_test}"

    const val android_test_junit =
        "androidx.test.ext:junit:${Version.androidx_test_junit}"

    const val junit =
        "junit:junit:${Version.junit}"

    const val truth =
        "com.google.truth:truth:${Version.truth}"

    const val arch_test =
        "androidx.arch.core:core-testing:${Version.arch}"

    const val mockk =
        "io.mockk:mockk:${Version.mockk}"
    //endregion

}
