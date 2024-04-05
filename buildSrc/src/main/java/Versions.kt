@Suppress("ConstPropertyName")
object Versions {

    object GradlePlugin {
        const val android = "8.3.0"
    }

    object Kotlin {
        const val kotlin = "1.9.0"
        const val coroutines = "1.6.0"
    }

    object Java {
        const val javaInject = 1
    }

    object AndroidX {
        //AndroidX
        const val coreKtx = "1.12.0"
        const val appCompat = "1.6.1"
        const val constraintlayout = "2.1.4"
        const val material = "1.11.0"
        const val activity = "1.8.0"
        const val workManager = "2.7.1"
        const val startup = "1.1.1"
        const val dataStore = "1.0.0"

        object Lifecycle {
            const val lifecycle = "2.5.1"
        }

        object Hilt {
            const val hilt = "1.0.0"
        }

        object Navigation {
            const val navigation = "2.5.3"
        }
    }

    object Dagger {
        const val hilt = "2.48"
    }

    object Test {
        const val junit = "4.13.2"
        const val androidJunit = "1.1.5"
        const val androidXEspressoCore = "3.5.1"
    }

    const val ktlint = "10.3.0"
    const val timber = "5.0.1"
    const val toasty = "1.5.2"
}
