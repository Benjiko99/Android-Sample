plugins {
    id("java-library")
    kotlin("jvm")
}
apply(plugin = "org.jmailen.kotlinter")

dependencies {
    implementation(Dependency.kotlin)
    implementation(Dependency.coroutines_core)
    implementation(Dependency.koin_core)

    testImplementation(Dependency.junit)
    testImplementation(Dependency.truth)
    testImplementation(Dependency.mockk)
    testImplementation(Dependency.koin_test)
    testImplementation(Dependency.coroutines_test)
}