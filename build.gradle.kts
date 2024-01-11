// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("lifecycle_version","2.6.1")
        set("reflect","1.8.22")
        set("retrofit","2.9.0")
        set("room","2.6.1")
        set("koin","3.2.2")
        set("core","1.12.0")
        set("appcompat","1.6.1")
        set("material","1.11.0")
        set("constraintlayout","2.1.4")
        set("junit","4.13.2")
        set("junitTest","1.1.5")
        set("espresso","3.5.1")
    }
}
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}