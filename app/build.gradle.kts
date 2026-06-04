import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.appdistribution")
}

android {
    namespace = "com.bungalow.budget"
    compileSdk {
        version = release(36)
    }

    val appVersionCode = (System.getenv("VERSION_CODE") ?: "1").toInt()
    val appVersionName = "1.0.$appVersionCode"

    defaultConfig {
        applicationId = "com.bungalow.budget"
        minSdk = 27
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystoreProperties = Properties()
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    if (keystorePropertiesFile.exists()) {
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    }

    signingConfigs {
        create("dev") {
            storeFile = file("dev-keystore")
            storePassword = "budget131pass"
            keyAlias = "budgetalias"
            keyPassword = "budget131pass"
        }

        create("prod") {
            storeFile = file(keystoreProperties.getProperty("STORE_FILE"))
            storePassword = keystoreProperties.getProperty("STORE_PASSWORD")
            keyAlias = keystoreProperties.getProperty("KEY_ALIAS")
            keyPassword = keystoreProperties.getProperty("KEY_PASSWORD")
        }
    }

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            versionName = "$appVersionName-dev"
        }

        create("beta") {
            dimension = "environment"
            versionName = "$appVersionName-beta"
        }

        create("prod") {
            dimension = "environment"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("prod")
            firebaseAppDistribution {
                artifactType = "APK"
                groups = "testers"
                releaseNotes = System.getenv("RELEASE_NOTES") ?: "Beta notes"
            }
        }

        debug {
            signingConfig = signingConfigs.getByName("dev")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation (libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.timber)
}