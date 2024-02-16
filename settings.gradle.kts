    pluginManagement {
    val kotlin_version: String by settings
    val compose_version: String by settings

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        id("org.jetbrains.kotlin.multiplatform") version kotlin_version
        id("org.jetbrains.compose") version compose_version
    }
}
rootProject.name = "BrickMultiplatform"

include(":desktop", ":android", ":brick")
