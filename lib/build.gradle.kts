plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.compose)
}

android {
    namespace = "com.example.lib"

    defaultConfig {
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

    implementation(libs.androidx.activity.compose)
    implementation(libs.material)

}