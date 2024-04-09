@Suppress("ConstPropertyName")
object Dependencies {
    object Classpath {
        const val androidApplication = "com.android.application"
        const val kotlinAndroid = "kotlin-android"
        const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.GradlePlugin.android}"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.kotlin}"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.Dagger.hilt}"
        const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.AndroidX.Navigation.navigation}"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:${Versions.AndroidX.coreKtx}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}"
        const val activity = "androidx.activity:activity:${Versions.AndroidX.activity}"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintlayout}"
        const val material = "com.google.android.material:material:${Versions.AndroidX.material}"
        const val activityKtx = "androidx.activity:activity-ktx:${Versions.AndroidX.activityKtx}"
        const val fragmentKtx = "androidx.activity:activity-ktx:${Versions.AndroidX.fragmentKtx}"
        const val workManager = "androidx.work:work-runtime-ktx:${Versions.AndroidX.workManager}"
        const val startup = "androidx.startup:startup-runtime:${Versions.AndroidX.startup}"
        const val dataStore = "androidx.datastore:datastore-preferences:${Versions.AndroidX.dataStore}"

        object Lifecycle {
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.Lifecycle.lifecycle}"
        }

        object Hilt {
            const val navigation = "androidx.hilt:hilt-navigation-fragment:${Versions.AndroidX.Hilt.hilt}"
            const val work = "androidx.hilt:hilt-work:${Versions.AndroidX.Hilt.hilt}"
            const val compiler = "androidx.hilt:hilt-compiler:${Versions.AndroidX.Hilt.hilt}" // For Kotlin use kapt instead of annotationProcessor
        }

        object Navigation {
            const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.AndroidX.Navigation.navigation}"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.Navigation.navigation}"
            const val navigationFragment = "androidx.navigation:navigation-fragment:${Versions.AndroidX.Navigation.navigationFragment}"
        }
    }

    object Kotlin {
        const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.Kotlin.kotlin}"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.coroutines}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.coroutines}"
        const val coroutinesServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.Kotlin.coroutines}"
    }

    object Java {
        const val javaInject = "javax.inject:javax.inject:${Versions.Java.javaInject}"
    }

    object Dagger {
        const val hilt = "com.google.dagger:hilt-android:${Versions.Dagger.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.Dagger.hilt}"
    }

    object Others {
        const val easyPermission = "pub.devrel:easypermissions:${Versions.Others.easyPermission}"
        const val timber = "com.jakewharton.timber:timber:${Versions.Others.timber}"
        const val toasty = "com.github.GrenderG:Toasty:${Versions.Others.toasty}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.Test.junit}"
        const val androidXJunit = "androidx.test.ext:junit:${Versions.Test.androidJunit}"
        const val androidXEspressoCore = "androidx.test.espresso:espresso-core:${Versions.Test.androidXEspressoCore}"
    }
}
