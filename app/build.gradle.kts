plugins {
    alias(libs.plugins.common.android.application)
    alias(libs.plugins.common.compose)
    alias(libs.plugins.common.lifecycle)
    alias(libs.plugins.common.hilt)
    alias(libs.plugins.common.navigation)
}

android {
    namespace = "com.sample.summary"

    defaultConfig {
        applicationId = "com.sample.summary"
        versionCode = 1
        versionName = "1.0"

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
    implementation(project(":core:ui"))
    implementation(project(":feature:book"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)

}