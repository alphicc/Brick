import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val kotlin_version: String by properties
val compose_version: String by properties
val coroutines_version: String by properties
val atomicfu_version: String by properties
val activity_compose_version: String by properties
val immutable_collections_version: String by properties
val VERSION_NAME: String by properties

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.vanniktech.maven.publish")
}

group = "io.github.alphicc"
version = VERSION_NAME

kotlin {
    android {
        publishAllLibraryVariants()
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "brick"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:${compose_version}")
                implementation("org.jetbrains.compose.foundation:foundation:${compose_version}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines_version}")
                implementation("org.jetbrains.kotlinx:atomicfu:${atomicfu_version}")
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:${immutable_collections_version}")

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:${activity_compose_version}")
            }
        }
    }
}

android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}
