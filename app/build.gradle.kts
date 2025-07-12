@file:OptIn(ExperimentalEncodingApi::class)

import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.GradleException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
}

android {
    namespace = "org.mattshoe.shoebox.listery"
    compileSdk = 35

    defaultConfig {
        val versionCodeProperty = project.findProperty("versionCode")?.toString()?.toIntOrNull() ?: 0
        applicationId = "org.mattshoe.shoebox.listery"
        minSdk = 28
        targetSdk = 35
        versionCode = versionCodeProperty
        versionName = "0.0.$versionCodeProperty"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            if (System.getenv("CI") == "true") {
                val keystoreBase64 = System.getenv("KEYSTORE_BASE64") ?: throw GradleException("Missing KEYSTORE_BASE64 environment variable for CI build")
                val keystorePassword = System.getenv("KEYSTORE_PASSWORD") ?: throw GradleException("Missing KEYSTORE_PASSWORD environment variable for CI build")
                val keyAlias = System.getenv("KEY_ALIAS") ?: throw GradleException("Missing KEY_ALIAS environment variable for CI build")
                val keyPassword = System.getenv("KEY_PASSWORD") ?: throw GradleException("Missing KEY_PASSWORD environment variable for CI build")

                val keystoreBytes = Base64.decode(keystoreBase64)
                val keystoreFile = File.createTempFile("keystore", ".jks")
                keystoreFile.writeBytes(keystoreBytes)

                storeFile = keystoreFile
                storePassword = keystorePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            } else {
                // Local builds will fail if trying to build release
                throw GradleException("Release builds are only allowed in CI environment")
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            setupFirebaseAppDistribution()
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            
            setupFirebaseAppDistribution()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    ksp(libs.hilt.compiler)

    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.openai.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.navigation)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.indriya)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
    implementation(libs.password4j)
    implementation(libs.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.remote.config)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.openai.client)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)


    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.reorderable)
}

private fun Project.setupFirebaseAppDistribution() {
    firebaseAppDistribution {
        artifactType = "APK"
        releaseNotesFile = "app/release_notes.txt"
        groups = "testers"
    }
}