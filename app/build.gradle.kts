plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "io.nerdythings.okprofilerexample"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    signingConfigs {
        create("configRelease") {
            storeFile = file("../testtest.jks")
            storePassword = "testtest"
            keyAlias = "testtest"
            keyPassword = "testtest"
        }
    }
    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("configRelease")
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("configRelease")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    namespace = "com.localebro.okprofilerexample"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    val compose: String = "1.6.8"

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")

    implementation("androidx.compose.material:material:$compose")
    implementation("androidx.compose.foundation:foundation:$compose")
    implementation("androidx.compose.foundation:foundation-layout:$compose")
    implementation("androidx.compose.runtime:runtime:$compose")
    implementation("androidx.compose.ui:ui:$compose")
    implementation("androidx.compose.ui:ui-tooling:$compose")

    implementation("androidx.activity:activity-compose:1.9.1")

    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.google.code.gson:gson:2.13.1")

    implementation("io.ktor:ktor-client-core:3.2.2")
    implementation("io.ktor:ktor-client-android:3.2.2")

//    implementation(project(":okhttp-profiler"))
//    releaseImplementation(project(":okhttp-requests-modifier-no-op"))
//    debugImplementation(project(":okhttp-requests-modifier"))

    releaseImplementation("io.nerdythings:okhttp-requests-modifier-no-op:1.0.2")
    debugImplementation("io.nerdythings:okhttp-requests-modifier:1.0.2")
    implementation("io.nerdythings:okhttp-profiler:1.1.1")
}