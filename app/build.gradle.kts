import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
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

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.auth)
    implementation(libs.play.services.auth)

    //credentials
    implementation (libs.androidx.credentials)
    implementation (libs.credentials.play.services.auth)
    implementation (libs.googleid)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //glide
    implementation(libs.github.glide)

    //shimmer
    implementation(libs.shimmer)

    //palette
    implementation(libs.androidx.palette)

    //dots indicator
    implementation(libs.dotsindicator)

    //lottie
    implementation(libs.lottie)
    implementation(libs.dotlottie.android)

    // OkHttp
    implementation(libs.okhttp)
    // Retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    // Gson for JSON parsing
    implementation(libs.gson)

    //maps
    implementation(libs.android.maps.utils)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.legacy.support.v4)

    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":features:test"))
    implementation(project(":data"))

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