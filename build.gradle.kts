import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension
import org.jmailen.gradle.kotlinter.KotlinterPlugin

buildscript {
    dependencies {
        classpath(Plugin.android_gradle)
        classpath(Plugin.google_services)
        classpath(Plugin.crashlytics)
        classpath(Plugin.kotlin)
        classpath(Plugin.kotlinter)
        classpath(Plugin.safeArgs)
        classpath(Plugin.exhaustive)
    }

    repositories {
        google()
        jcenter()
        maven(uri("https://plugins.gradle.org/m2/"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
}

subprojects {
    plugins.withType<KotlinterPlugin> {
        the<KotlinterExtension>().disabledRules = arrayOf(
            "no-wildcard-imports",
            "final-newline",
            "comment-spacing",
            "no-blank-line-before-rbrace",
            "no-multi-spaces"
        )
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = AppConfig.jvmTarget.toString()
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    tasks.withType<Test> {
        testLogging.showStandardStreams = true
        testLogging.exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}