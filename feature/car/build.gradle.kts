plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.compose)
    alias(libs.plugins.common.hilt)
    alias(libs.plugins.common.lifecycle)
}

android {
    namespace = "com.sample.feature.car"

    defaultConfig {

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

}
dependencies {

    implementation(project(":data:repository"))
    implementation(project(":core:ui"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}