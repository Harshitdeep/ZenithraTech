plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" // ✅ Add this line
    id("com.google.devtools.ksp")
    id("kotlin-kapt")                      // Needed for Hilt

}

android {
    namespace = "com.example.zenithra"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.zenithra"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.21" // or your latest
    }

}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui:1.8.0")
    implementation("androidx.compose.material:material:1.8.0")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.room:room-runtime:2.7.1")
    ksp("androidx.room:room-compiler:2.7.1")
    implementation("androidx.room:room-ktx:2.7.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")



    implementation("io.coil-kt:coil-compose:2.4.0")

//    implementation("com.google.dagger:hilt-android:2.49")
//    ksp("com.google.dagger:hilt-compiler:2.48")
//    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    implementation("androidx.camera:camera-view:1.4.2")

    implementation("androidx.camera:camera-camera2:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.camera:camera-core:1.4.2")

    implementation("com.google.mlkit:face-detection:16.1.7")


    implementation("androidx.compose.runtime:runtime:1.8.0")
    implementation("androidx.compose.ui:ui-tooling:1.8.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")


}