plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.myapp.dreamystory"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.myapp.dreamystory"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
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
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

    configurations { implementation.get().exclude(mapOf("group" to "org.jetbrains", "module" to "annotations")) }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.glide)

    implementation(libs.androidx.camera.view)

    implementation(libs.androidx.exifinterface)

    implementation(libs.play.services.maps.v1800)
    implementation(libs.play.services.location)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.runtime.ktx.v300)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.compiler)
    kapt("androidx.room:room-compiler:2.6.1")

    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation(libs.kotlinx.coroutines.test)

    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
}