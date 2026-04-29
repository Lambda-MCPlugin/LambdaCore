plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.6"
}

dependencies {
    implementation(project(":core-api"))
    implementation(project(":core-common"))
    implementation(project(":platform-folia"))

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("com.zaxxer:HikariCP:6.3.3")
    implementation("com.mysql:mysql-connector-j:9.7.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.8")
    implementation("org.xerial:sqlite-jdbc:3.53.0.0")

    implementation(project(":nms:v1_21_R1"))
    implementation(project(":nms:v1_21_R2"))

    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveBaseName.set("LambdaCore")
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())

        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}