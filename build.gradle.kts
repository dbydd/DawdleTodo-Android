// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    dependencies{
        classpath("io.realm:realm-gradle-plugin:10.18.0")
    }
}

allprojects{
    repositories{
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
}