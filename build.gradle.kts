plugins {
    kotlin("jvm") version "2.2.21" apply false
}

subprojects {
    group = "lambda.core"
    version = "1.0.0"

    plugins.withId("org.jetbrains.kotlin.jvm") {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
            jvmToolchain(21)
        }
    }
}