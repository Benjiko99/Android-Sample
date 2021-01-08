import org.jmailen.gradle.kotlinter.KotlinterExtension
import org.jmailen.gradle.kotlinter.KotlinterPlugin

buildscript {
    val kotlin_version by extra("1.4.21")
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath(kotlin("gradle-plugin", version = Dependencies.kotlin))
        classpath("org.jetbrains.kotlin:kotlin-allopen:${Dependencies.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Dependencies.navigation}")
        classpath("org.jmailen.gradle:kotlinter-gradle:${Dependencies.kotlinter}")
        classpath("app.cash.exhaustive:exhaustive-gradle:${Dependencies.exhaustive}")
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
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
