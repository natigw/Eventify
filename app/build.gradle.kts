import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("androidx.navigation.safeargs.kotlin")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.eventify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.eventify"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { inputStream ->
                localProperties.load(inputStream)
            }
        }

        buildConfigField("String", "MAPS_API_KEY", "\"${localProperties["MAPS_API_KEY"]}\"")
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    //credentials
    implementation (libs.androidx.credentials)
    implementation (libs.credentials.play.services.auth)
    implementation (libs.googleid)

    //dots indicator
    implementation(libs.dotsindicator)

    //hilt
    implementation(libs.hilt.android)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.legacy.support.v4)
    implementation(project(":data"))
    kapt(libs.hilt.android.compiler)

    //glide
    implementation(libs.github.glide)

    //palette
    implementation(libs.androidx.palette)

    //retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    // OkHttp
    implementation(libs.okhttp)

    implementation(libs.android.maps.utils)





    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":features:test"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes=true
}