// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.gradle.api.initialization.resolve.RepositoriesMode

buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
}