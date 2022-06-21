val kotlin_version: String by properties
val compose_version: String by properties
val coroutines_version: String by properties
val atomicfu_version: String by properties
val activity_compose_version: String by properties
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
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:${compose_version}")
                implementation("org.jetbrains.compose.foundation:foundation:${compose_version}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines_version}")
                implementation("org.jetbrains.kotlinx:atomicfu:${atomicfu_version}")
            }
        }
       // val commonTest by getting {
       //     dependencies {
       //     }
       // }
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:${activity_compose_version}")
            }
        }
      //  val androidTest by getting {
      //      dependencies {
      //      }
      //  }
        val desktopMain by getting {
            dependencies {
            }
        }
      //  val desktopTest by getting
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}