plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.soundslike"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.soundslike"
        minSdk = 24
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")

    val lifecycleVersion = "2.8.3"
    val fragmentVersion = "1.8.1"
    implementation("androidx.fragment:fragment:$fragmentVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycleVersion")

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    implementation("androidx.media3:media3-session:1.3.1")

    // Retrofit & Gson Converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Use latest 2.x version
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1") // Or latest

    // OkHttp Logging Interceptor (for debugging network calls)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // Use compatible version with Retrofit

    // Glide for Image Loading
    implementation("com.github.bumptech.glide:glide:4.15.1") // Use latest version
    // annotationProcessor("com.github.bumptech.glide:compiler:4.15.1") // Use kapt if using Kotlin annotation processing

    // Coroutines for background tasks (if not already present)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Use latest version
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3") // For viewModelScope


}