plugins {
    id("com.android.application")
    kotlin("android")
}

group = "io.github.alphicc"
version = "1.0"

repositories {
}

dependencies {
    //implementation("io.github.alphicc:brick:1.0.0-beta03")
    implementation(project(":brick"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation ("androidx.compose.material:material:1.4.3")
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "io.github.alphicc.android"
        minSdk = 21
        targetSdk = 34
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
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}