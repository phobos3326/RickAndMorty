plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.androidx.navigation.safeargs)


}

android {
    namespace = "com.example.rickandmorty"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.rickandmorty"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit)

    // moshi
    implementation (libs.moshi.kotlin)
    implementation (libs.converter.moshi)
    ksp (libs.moshi.kotlin.codegen)


    // logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // hilt
    implementation(libs.hilt.android)
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    ksp(libs.hilt.compiler)

    // ROOM
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    ksp ("androidx.room:room-compiler:2.5.0")
    implementation("androidx.room:room-paging:2.5.0")
    implementation("androidx.paging:paging-runtime:3.1.1")

    // Optional: Room testing support
    testImplementation("androidx.room:room-testing:${libs.versions.room.get()}")

    // coil
    implementation(libs.coil)
    implementation("io.coil-kt:coil-compose:2.5.0")


    // navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)


    // paging
    implementation("androidx.paging:paging-runtime:3.1.0")
    implementation("androidx.paging:paging-compose:3.3.6")





}