plugins {
    id("com.android.application")
    kotlin("android")
}

group = "io.github.alphicc"
version = "1.0"

repositories {
}

dependencies {
    implementation("io.github.alphicc:brick:1.0.0-beta03")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}