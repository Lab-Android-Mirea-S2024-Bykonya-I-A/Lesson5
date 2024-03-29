pluginManagement {
    repositories {
        google()
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

rootProject.name = "ru.mirea.Bykonya.Lesson5"
include(":app")
include(":app:task_1_1")
include(":app:accelerometer")
include(":app:camera")
include(":app:audiorecord")
include(":app:mireaproject")
