import com.sample.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class NavigationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "androidx.navigation.safeargs.kotlin")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                //kotlin序列号库,需要添加插件kotlin-serialization
                "implementation"(libs.findLibrary("kotlinx-serialization-json").get())
                "implementation"(libs.findLibrary("androidx-navigation-ui-ktx").get())
                "implementation"(libs.findLibrary("androidx-navigation-fragment-ktx").get())
                "implementation"(libs.findLibrary("androidx-navigation-compose").get())
                "androidTestImplementation"(libs.findLibrary("androidx-navigation-testing").get())
            }
        }
    }
}
