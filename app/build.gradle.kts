plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.project.digitalbank"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.digitalbank"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }

}

dependencies {

    // firebase
    implementation(platform(libs.firebase.bom))
    //authentication
    implementation(libs.firebase.ui.auth)
    //realtime database
    implementation("com.google.firebase:firebase-database")
    // Add the dependency for the Cloud Storage library
    implementation("com.google.firebase:firebase-storage")

    // dependecy injection c hilt
    implementation(libs.hilt.android.v2481)
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.hilt.android.compiler)

    // ViewModel
    implementation(libs.lifecycle.viewmodel.ktx)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //masked edit text - https://github.com/VicMikhailau/MaskedEditText
    implementation("io.github.vicmikhailau:MaskedEditText:5.0.1")

    // shaped image view - https://github.com/cheonjaeung/shapedimageview
    implementation("io.woong.shapedimageview:shapedimageview:1.4.3")

    // picasso library - https://github.com/square/picasso
    implementation("com.squareup.picasso:picasso:2.8")

    // Ted permission - https://github.com/ParkSangGwon/TedPermission
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

    // simple search view library - https://github.com/Ferfalk/SimpleSearchView
    implementation("com.github.Ferfalk:SimpleSearchView:0.2.1")

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
