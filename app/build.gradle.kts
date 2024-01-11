plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
}

android {
    namespace = "com.bejussi.currencyexchangertesttask"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bejussi.currencyexchangertesttask"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    val lifecycle_version = rootProject.extra["lifecycle_version"]
    val reflect = rootProject.extra["reflect"]
    val retrofit = rootProject.extra["retrofit"]
    val room = rootProject.extra["room"]
    val koin = rootProject.extra["koin"]
    val core = rootProject.extra["core"]
    val appcompat = rootProject.extra["appcompat"]
    val material = rootProject.extra["material"]
    val constraintlayout = rootProject.extra["constraintlayout"]
    val junit = rootProject.extra["junit"]
    val junitTest = rootProject.extra["junitTest"]
    val espresso = rootProject.extra["espresso"]

    implementation("androidx.core:core-ktx:$core")
    implementation("androidx.appcompat:appcompat:$appcompat")
    implementation("com.google.android.material:material:$material")
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayout")
    testImplementation("junit:junit:$junit")
    androidTestImplementation("androidx.test.ext:junit:$junitTest")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso")

    // Koin
    implementation("io.insert-koin:koin-core:$koin")
    implementation("io.insert-koin:koin-android:$koin")

    //Room
    implementation("androidx.room:room-runtime:$room")
    ksp("androidx.room:room-compiler:$room")
    implementation ("androidx.room:room-ktx:$room")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit")

    //Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:$reflect")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
}