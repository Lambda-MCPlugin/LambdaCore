pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "LambdaCore"

include(
    "core-api",
    "core-common",
    "platform-folia",
    "plugin",
    "nms:v1_21_R1",
    "nms:v1_21_R2"
)