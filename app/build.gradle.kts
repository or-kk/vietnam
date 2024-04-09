plugins {
    id(Config.Plugins.androidApplication)
    id(Config.Plugins.kotlinAndroid)
    id(Config.Plugins.hiltAndroid)
    id(Config.Plugins.kotlinKapt)
    id(Config.Plugins.navigationSafeArgs)
}

android {
    namespace = Config.AppConfig.applicationId
    compileSdk = Config.AppConfig.compileSdk

    defaultConfig {
        applicationId = Config.AppConfig.applicationId
        minSdk = Config.AppConfig.minSdk
        targetSdk = Config.AppConfig.compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = Config.AppConfig.testRunner
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = Config.AppConfig.jvmTarget
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
}

dependencies {
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.material)
    implementation(Dependencies.AndroidX.activity)
    implementation(Dependencies.AndroidX.activityKtx)
    implementation(Dependencies.AndroidX.fragmentKtx)
    implementation(Dependencies.AndroidX.constraintlayout)
    implementation(Dependencies.AndroidX.Navigation.ui)
    implementation(Dependencies.AndroidX.Navigation.fragment)
    implementation(Dependencies.AndroidX.Navigation.navigationFragment)
    implementation(Dependencies.Dagger.hilt)
    kapt(Dependencies.Dagger.hiltCompiler)
    implementation(Dependencies.Others.easyPermission)
    implementation(Dependencies.Others.timber)

    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.androidXJunit)
    androidTestImplementation(Dependencies.Test.androidXEspressoCore)
}