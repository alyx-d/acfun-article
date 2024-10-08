@file:Suppress("UnstableApiUsage")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AcfunArticle"
include(":app")
include(":core:ui-state")
include(":core:data")
include(":core:database")
include(":core:network")
include(":core:navigation")
include(":core:video-player")
include(":feature:video")
include(":feature:article")
include(":feature:dynamic")
include(":feature:profile")
include(":feature:splash")
