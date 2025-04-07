import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.22" apply false
    `kotlin-dsl`
}


apply(from = "$rootDir/environment.gradle.kts")

allprojects {
    configureAndroid()
}

fun Project.configureAndroid() {
    val tree = (group as String).split(".")

    when {
        tree.contains("library") -> applyLibraryPlugins()
        tree.contains("app") -> applyAppPlugins()
        tree.contains("examples") -> applyAppPlugins()
        tree.contains("feature") -> apply(plugin = "com.android.dynamic-feature")
        else -> return
    }

    configureBaseExtension()

    when {
        tree.contains("library") -> configureLibraryExtension()
        tree.contains("app") || tree.contains("examples") -> configureAppExtension()
        else -> println("Tree contains no modules to apply config")
    }
}

fun Project.applyLibraryPlugins() {
    apply(plugin = "com.android.library")
    apply(plugin = "kotlin-android")
    apply(plugin = "org.jetbrains.compose")
    apply(plugin = "org.jetbrains.kotlin.plugin.compose")
    apply(plugin = "com.google.dagger.hilt.android")
    apply(plugin = "com.google.devtools.ksp")
}

fun Project.applyAppPlugins() {
    apply(plugin = "com.android.application")
    apply(plugin = "kotlin-android")
    apply(plugin = "org.jetbrains.compose")
    apply(plugin = "org.jetbrains.kotlin.plugin.compose")
    apply(plugin = "com.google.dagger.hilt.android")
    apply(plugin = "com.google.devtools.ksp")
}

fun Project.configureBaseExtension() {
    configure<BaseExtension> {
        defaultConfig {
            minSdk = rootProject.extra["baseMinSDK"] as Int
            versionCode = rootProject.extra["versionCode"] as Int
            versionName = rootProject.extra["versionName"].toString()
            vectorDrawables.useSupportLibrary = true

            buildConfigField(
                "com.fermion.android.base.config.Environment",
                "ENVIRONMENT",
                "com.fermion.android.base.config.Environment.${rootProject.extra["environment"]}"
            )

            buildConfigField("String", "BASE_API_URL", "\"${rootProject.extra["baseUrl"]}\"")
            buildConfigField(
                "String",
                "sslPinnerSha256",
                "\"${rootProject.extra["sslPinnerSha256"]}\""
            )
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        (this as ExtensionAware).configure<KotlinJvmOptions> {
            jvmTarget = "17"
        }
        buildFeatures.apply {
            buildConfig = true
            viewBinding = true
        }
    }

}

fun Project.configureLibraryExtension() {
    configure<LibraryExtension> {
        compileSdk = rootProject.extra["baseCompileSDK"] as Int
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            consumerProguardFiles("consumer-rules.pro")
        }
        lint {
            checkReleaseBuilds = true
        }
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.1"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

    }

    dependencies {
    }
    apply(from = "$rootDir/dependencies.gradle")

}

fun Project.configureAppExtension() {
    configure<BaseAppModuleExtension> {
        compileSdk = rootProject.extra["baseCompileSDK"] as Int
        defaultConfig {
            targetSdk = rootProject.extra["baseTargetSDK"] as Int
            minSdk = rootProject.extra["baseMinSDK"] as Int
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            versionCode = rootProject.extra["versionCode"] as Int
            versionName = rootProject.extra["versionName"].toString()
            vectorDrawables.useSupportLibrary = true
        }
        lint {
            checkReleaseBuilds = true
        }
        applicationVariants.all {
            outputs.all {
                val appName = "app"
                val env = rootProject.extra["environment"].toString().lowercase()
                val apkFileName = "$appName-$env-v$versionName-${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_hh-mm"))
                }"

                when (buildType.name) {
                    "debug" -> (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                        "debug-$apkFileName.apk"

                    "release" -> (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                        "$apkFileName.apk"
                }
            }
        }

        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.1"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }


    apply(from = "$rootDir/dependencies.gradle")

    dependencies {
        "implementation"(project(mapOf("path" to ":library:base")))
    }

}
