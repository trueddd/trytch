import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
    id("kotlin-parcelize")
}

android {
    namespace = "${Config.PackageName}.twitch"
    compileSdk = Config.Android.CompileSdk

    defaultConfig {
        minSdk = Config.Android.MinSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val twitchKeys = getTwitchKeys()
        buildConfigField("String", "twitchClientId", "\"${twitchKeys.clientId}\"")
        buildConfigField("String", "twitchClientSecret", "\"${twitchKeys.clientSecret}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Versions.Java
        targetCompatibility = Versions.Java
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(Dependencies.Core.KotlinImmutable)
    implementation(Dependencies.Koin.Core)
    implementation(Dependencies.Koin.Annotations)
    ksp(Dependencies.Koin.Compiler)

    implementation(Dependencies.Ktor.Core)
    implementation(Dependencies.Ktor.OkHttp)
    implementation(Dependencies.Ktor.OkHttpLogging)
    implementation(Dependencies.Ktor.ContentNegotiation)
    implementation(Dependencies.Ktor.KotlinX)
    implementation(Dependencies.Ktor.Auth)
    implementation(files(projectDir.resolve("libs").resolve("tmik-jvm-0.1.3.jar")))

    implementation(Dependencies.Room.Core)
    implementation(Dependencies.Room.Ktx)
    ksp(Dependencies.Room.Compiler)

    api(Dependencies.DataStore)
}

fun getTwitchKeys(): TwitchKeys {
    val propertiesFile = rootProject.file("twitch.properties")
    val properties = Properties().apply { load(FileInputStream(propertiesFile)) }
    return TwitchKeys(
        properties["client_id"].toString(),
        properties["client_secret"].toString(),
    )
}
