plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.eventify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.eventify"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }
    buildTypes {
//        debug {
//            buildConfigField("String", "MAPS_API_KEY", "\"${project.properties['MAPS_API_KEY']}\"")
//        }
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

    //dots indicator
    implementation(libs.dotsindicator)

    //hilt
    implementation(libs.hilt.android)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.legacy.support.v4)
    kapt(libs.hilt.android.compiler)

    //fancy toast
    implementation(libs.github.fancytoast)

    //glide
    implementation(libs.github.glide)

    //retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)

    implementation("com.google.maps.android:android-maps-utils:2.4.0")

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
    correctErrorTypes = true
}