// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
        classpath(kotlin("gradle-plugin", version = Deps.kotlin))
        classpath("org.jetbrains.kotlin:kotlin-allopen:${Deps.kotlin}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Deps.navigation}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
