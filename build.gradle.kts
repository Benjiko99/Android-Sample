import org.jmailen.gradle.kotlinter.KotlinterExtension
import org.jmailen.gradle.kotlinter.KotlinterPlugin

buildscript {
    dependencies {
        classpath(Plugin.androidGradle)
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
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
