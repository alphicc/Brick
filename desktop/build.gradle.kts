import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.5.10"
}

group = "io.github.alphicc"
version = "2.4.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":brick"))

                //implementation("io.github.alphicc:brick:1.0.0-beta03")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.2")
                implementation(compose.desktop.currentOs)
                api(compose.preview)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}