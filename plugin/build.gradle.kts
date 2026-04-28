plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core-api"))
    implementation(project(":core-common"))
    implementation(project(":platform-folia"))

    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}

tasks.jar {
    archiveBaseName.set("LambdaCore")
}