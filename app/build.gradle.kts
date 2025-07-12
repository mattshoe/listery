import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    id("com.gladed.androidgitversion")
}

android {
    namespace = "org.mattshoe.shoebox.listery"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.mattshoe.shoebox.listery"
        minSdk = 28
        targetSdk = 35
        versionCode = androidGitVersion.code()
        versionName = androidGitVersion.name()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            // Try to find keystore.properties in multiple locations
            val keystorePropertiesFile = when {
                rootProject.file("keystore.properties").exists() -> rootProject.file("keystore.properties")
                file("keystore.properties").exists() -> file("keystore.properties")
                else -> null
            }
            
            if (keystorePropertiesFile != null) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            } else {
                // For local development without keystore, use debug signing
                println("Warning: keystore.properties not found. Using debug signing for release builds.")
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
            // Only use release signing if keystore is available
            val releaseSigningConfig = signingConfigs.findByName("release")
            if (releaseSigningConfig != null && releaseSigningConfig.storeFile != null) {
                signingConfig = releaseSigningConfig
            }
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