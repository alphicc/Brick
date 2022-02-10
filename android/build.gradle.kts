plugins {
    id("com.android.application")
    kotlin("android")
}

group = "io.github.alphicc"
version = "1.0"

repositories {
}

dependencies {
    implementation(project(":brick"))
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation ("androidx.compose.material:material:1.1.0")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "io.github.alphicc.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-rc03"
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}