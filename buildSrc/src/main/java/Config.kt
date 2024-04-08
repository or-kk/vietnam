@Suppress("ConstPropertyName")
object Config {
    object AppConfig {
        const val applicationId = "io.orkk.vietnam"
        const val minSdk = 24
        const val compileSdk = 34
        const val versionCode = 1
        const val versionName = "1.0"
        const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val jvmTarget = "1.8"
    }

    object Plugins {
        const val androidLibrary = "com.android.library"
        const val androidApplication = "com.android.application"
        const val kotlin = "kotlin"
        const val kotlinAndroid = "kotlin-android"
        const val kotlinKapt = "kotlin-kapt"
        const val hiltAndroid = "dagger.hilt.android.plugin"
        const val javaLibrary = "java-library"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
        const val navigationSafeArgs = "androidx.navigation.safeargs.kotlin"
        const val parcelize = "kotlin-parcelize"
    }
}
