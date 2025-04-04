plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.kyscanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kyscanner"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {

    // Core AndroidX and UI
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    // Lifecycle Components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Use the latest stable version
    implementation("androidx.lifecycle:lifecycle-process:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime:2.7.0")

    // CameraX Dependencies (Latest Stable Versions)
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")

    // ML Kit Barcode Scanner (Google Play Services)
    implementation("com.google.mlkit:barcode-scanning:17.2.0") // Avoid using multiple barcode SDKs
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.1")
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")

    // Networking (Retrofit + OkHttp)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Image Loading Libraries (Choose one: Picasso or Glide)
    implementation("com.github.bumptech.glide:glide:4.15.1") // Recommended over Picasso
    // implementation("com.squareup.picasso:picasso:2.71828") // Comment this if using Glide

    // QR Code Scanner (ZXing)
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.squareup.picasso:picasso:2.8")

    //Authentication Handling
    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")


    // WorkManager for Background Tasks
    implementation("androidx.work:work-runtime:2.8.1")
    implementation ("com.google.code.gson:gson:2.8.9")

    // Animations
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("androidx.compose.animation:animation:1.7.8")
    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")

    // Unit & UI Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}