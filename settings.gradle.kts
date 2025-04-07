pluginManagement {
    includeBuild("build-logic")
    repositories {
        // Google Maven 仓库 (比如 AGP 插件用)
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // Maven Central 也可以用来拉部分插件
        mavenCentral()
        // 官方插件仓库
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    /**
     * 强制所有仓库声明必须写在 settings.gradle 中，如果某个 module 的 build.gradle 里还写了 repositories {}，Gradle 会直接报错！
     * repositoriesMode 控制 Gradle 是否允许在各个 module 的 build.gradle 文件中声明自己的仓库。
     * 具体值有几个选项：
     * RepositoriesMode.PREFER_SETTINGS	如果 settings 中有仓库配置，优先使用 settings.gradle 中的 repositories，忽略 module 里的。
     * RepositoriesMode.FAIL_ON_PROJECT_REPOS	禁止在 module 中声明仓库，如果 module 的 build.gradle 里写了 repositories {}，直接编译失败。
     * RepositoriesMode.ALLOW_PROJECT_REPOS	允许 module 的 build.gradle 里自己声明仓库，和 settings 中的并存。
     */
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            /**
             * content {} 是用来 过滤/限制这个仓库能提供哪些 groupId 依赖 的！
             * 也就是说，你可以告诉 Gradle：
             * “这个仓库（比如 Google Maven 仓库），只用于解析哪些特定 groupId，其他的不要从这里找，避免浪费时间。
             * 只有 groupId 以 com.android.*、com.google.* 或 androidx.* 开头的依赖，才会从 Google 仓库下载！
             */
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com\\.google.*")
            }
        }
        mavenCentral()
        // 需要私有仓库的可以额外添加
        // maven {
        //     url = uri("https://your.private.repo")
        //     credentials { username = "xxx"; password = "xxx" }
        //     content {
        //         includeGroup("com.yourcompany")
        //     }
        // }
    }
}

rootProject.name = "SampleSummary"
include(":app")
include(":core:ui")
include(":core:util")
include(":data:database")
include(":data:repository")
include(":feature:car")
include(":feature:book")
include(":feature:upgrade")
