import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.github.trueddd.twitch"
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
    implementation(Dependency.Koin.Core)
    implementation(Dependency.Koin.Annotations)
    ksp(Dependency.Koin.Compiler)

    implementation(Dependency.Ktor.Core)
    implementation(Dependency.Ktor.OkHttp)
    implementation(Dependency.Ktor.OkHttpLogging)
    implementation(Dependency.Ktor.ContentNegotiation)
    implementation(Dependency.Ktor.KotlinX)
    implementation(Dependency.Ktor.Auth)
    implementation(files(projectDir.resolve("libs").resolve("tmik-jvm-0.1.3.jar")))

    implementation(Dependency.Room.Core)
    implementation(Dependency.Room.Ktx)
    ksp(Dependency.Room.Compiler)

    api(Dependency.DataStore)
}

fun getTwitchKeys(): TwitchKeys {
    val propertiesFile = rootProject.file("twitch.properties")
    val properties = Properties().apply { load(FileInputStream(propertiesFile)) }
    return TwitchKeys(
        properties["client_id"].toString(),
        properties["client_secret"].toString(),
    )
}
