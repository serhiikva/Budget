plugins {
    alias(libs.plugins.android.library)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.investigate.remotefirebase"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "FIREBASE_BUDGETS_REF", "\"budgets_dev\"")
        buildConfigField("String", "FIREBASE_USERS_REF", "\"users_dev\"")
    }

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            buildConfigField("String", "FIREBASE_BUDGETS_REF", "\"budgets_dev\"")
            buildConfigField("String", "FIREBASE_USERS_REF", "\"users_dev\"")
        }

        create("prod") {
            dimension = "environment"
            buildConfigField("String", "FIREBASE_BUDGETS_REF", "\"budgets\"")
            buildConfigField("String", "FIREBASE_USERS_REF", "\"users\"")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)

    implementation (libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.timber)
}