plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":core-api"))
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}