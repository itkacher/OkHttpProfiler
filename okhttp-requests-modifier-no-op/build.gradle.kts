plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
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
    namespace = "io.nerdythings.okhttp.requests.modifier"
}

dependencies {
    compileOnly("com.squareup.okhttp3:okhttp:4.12.0")
}

val artifactId = "okhttp-requests-modifier-no-op"
val releaseVersion = "1.0.2"
val libraryDescription = "Empty implementation of the okhttp request modifier library. Should be used in release builds."

//apply(from = "${rootDir}/publish/publish.gradle.kts")