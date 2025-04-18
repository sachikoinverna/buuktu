plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.buuktu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.buuktu"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
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
    implementation(libs.material) // Usa la versión más reciente de Material Components
    implementation(libs.swiperefreshlayout)
    implementation(libs.activity.v1101)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation (libs.activity.v170)
    implementation(libs.appcompat)
    implementation (libs.lottie) // o la versión más reciente
    implementation (libs.rxandroid) // o la versión más reciente
    implementation (libs.rxjava)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.cardview) // Usa la versión actual de androidx.cardview
    implementation(libs.gridlayout)
    implementation(libs.preference)
    testImplementation(libs.junit)
    implementation(libs.glide) // Usa la versión más reciente
    annotationProcessor(libs.compiler) // Si usas Annotation Processing
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
