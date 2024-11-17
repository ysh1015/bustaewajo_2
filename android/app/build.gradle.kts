import com.github.javaparser.printer.lexicalpreservation.DifferenceElement

plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.hk.transportProject"
    compileSdk = 34

    viewBinding { enable = true }
    dataBinding { enable = true }

    defaultConfig {
        applicationId = "com.hk.transportProject"
        minSdk = 28
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.retrofit)        // Retrofit 의존성 추가
    implementation(libs.retrofit.gson)   // Gson 컨버터 의존성 추가
    implementation(libs.naver.map.sdk)      // 네이버 지도 SDK 의존성 추가
    implementation(libs.naver.map.service)  // 네이버 지도 위치기반 의존성

    // TikXML 의존성 추가
    implementation(libs.tikxml.annotation)
    implementation(libs.tikxml.core)
    implementation(libs.tikxml.retrofit.converter)
    implementation(libs.databinding.runtime)
    annotationProcessor(libs.tikxml.processor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}