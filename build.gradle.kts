buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.Classpath.androidGradlePlugin)
        classpath(Dependencies.Classpath.kotlinGradlePlugin)
        classpath(Dependencies.Classpath.hiltGradlePlugin)
        classpath(Dependencies.Classpath.navigationSafeArgs)
        classpath(Dependencies.Classpath.googleService)
    }
}