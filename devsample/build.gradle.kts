plugins {
    alias(libs.plugins.common.android.application)
    alias(libs.plugins.common.lifecycle)
    alias(libs.plugins.common.hilt)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.sample.dev"

    defaultConfig {
        applicationId = "com.sample.dev"
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
    implementation(project(":core:basic"))
    implementation(project(":dev:pagingsample"))
    implementation(project(":dev:pagingbasic"))
    implementation(project(":dev:pagingadvanced"))
    implementation(project(":dev:pagingreddit"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
}