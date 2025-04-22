plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.lifecycle)
    alias(libs.plugins.common.paging)
}

android {
    namespace = "com.sample.core.demo"

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
    implementation(libs.material)
    implementation(libs.retrofit)
}