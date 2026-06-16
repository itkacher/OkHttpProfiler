plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    namespace = "io.nerdythings.okhttp.profiler"
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    compileOnly("com.squareup.okhttp3:okhttp:4.12.0")
}

val artifactId = "okhttp-profiler"
val releaseVersion = "1.1.1"
val libraryDescription = "This library is using for Android Studio plugin Okhttp Profiler"

//apply(from = "${rootDir}/publish/publish.gradle.kts")