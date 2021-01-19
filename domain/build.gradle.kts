plugins {
    id("java-library")
    kotlin("jvm")
}
apply(plugin = "org.jmailen.kotlinter")

dependencies {
    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.koin_core)
    implementation(Dependency.threetenabp)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.coroutines_test)
    testImplementation(Dependency.mockito_inline)
    testImplementation(Dependency.mockito_kotlin)
    testImplementation("org.threeten:threetenbp:${Version.threetenabp}") {
        exclude("com.jakewharton.threetenabp", "threetenabp")
    }
}