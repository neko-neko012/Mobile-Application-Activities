plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.sipaglang"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sipaglang"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.biometric)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Runtime
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

   //LifecycleRuntime
    implementation(libs.lifecycle.runtime)

    // Fingerprint Authentication
    implementation("androidx.security:security-crypto:1.0.0")

    // Glide (Image Loading)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    implementation(libs.google.nearby)
    implementation(libs.gson)

}
