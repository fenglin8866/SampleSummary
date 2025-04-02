import com.sample.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


class PagingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(libs.findLibrary("androidx.paging.runtime.ktx").get())
                //paging比较少用，暂不添加到compose插件中
                "implementation"(libs.findLibrary("androidx.paging.compose").get())
                "testImplementation "(libs.findLibrary("androidx.paging.common").get())

                "implementation"(libs.findLibrary("androidx.room.paging").get())
            }
        }
    }
}