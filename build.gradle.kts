import org.jmailen.gradle.kotlinter.KotlinterExtension
import org.jmailen.gradle.kotlinter.KotlinterPlugin

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath(kotlin("gradle-plugin", version = Deps.kotlin))
        classpath("org.jetbrains.kotlin:kotlin-allopen:${Deps.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Deps.navigation}")
        classpath("org.jmailen.gradle:kotlinter-gradle:${Deps.kotlinter}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
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
