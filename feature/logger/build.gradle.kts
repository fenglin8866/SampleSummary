plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.lifecycle)
    alias(libs.plugins.common.hilt)
    alias(libs.plugins.common.navigation)
    alias(libs.plugins.common.room)
}

android {
    namespace = "com.sample.feature.logger"

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
    implementation(project(":core:basic"))
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.material)
    implementation(libs.androidx.fragment)
}