plugins {
    id("com.android.application") version Versions.AndroidGradlePlugin apply false
    id("com.android.library") version Versions.AndroidGradlePlugin apply false
    id("org.jetbrains.kotlin.android") version Versions.Kotlin apply false
    id("com.google.devtools.ksp") version Versions.Ksp apply false
    kotlin("plugin.serialization") version Versions.Kotlin apply false
}
