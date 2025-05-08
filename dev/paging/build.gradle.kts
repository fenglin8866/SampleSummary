plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.lifecycle)
    alias(libs.plugins.common.paging)
    alias(libs.plugins.common.room)
}

android {
    namespace = "com.sample.dev.paging"

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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.material)
    implementation(libs.retrofit)
}