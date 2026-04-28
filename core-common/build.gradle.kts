plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":core-api"))
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    implementation("com.zaxxer:HikariCP:6.3.3")
    implementation("com.mysql:mysql-connector-j:9.7.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.8")
    implementation("org.xerial:sqlite-jdbc:3.53.0.0")
}