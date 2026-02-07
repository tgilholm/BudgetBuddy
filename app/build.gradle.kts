plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android") version "2.59.1" apply false
    id("com.google.devtools.ksp") version "2.3.5"
}

android {
    namespace = "com.example.budgetbuddy"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.budgettracker"
        minSdk = 35 // Minimum SDK updated to support java.time and getLast()
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.viewpager2) // ViewPager2
    implementation(libs.mpandroidchart) // MPAndroidChart

    // Room DB
    implementation(libs.room.runtime)
    implementation(libs.recyclerview)

    // DataStore
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.preferences.core)
    implementation(libs.datastore)
    implementation(libs.datastore.core)


    // Preference Fragment
    implementation(libs.preference)
    implementation(libs.core)
    implementation(libs.ext.junit)
    implementation(libs.core.ktx)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
}