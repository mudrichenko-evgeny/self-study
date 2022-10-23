plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
    id(Plugins.hilt)
    id(Plugins.navigationSafeArgs)
    id(Plugins.parcelize)
}

android {
    val appVersionCode = ApplicationConfig.Version.code
    val appVersionNumber = ApplicationConfig.Version.number

    compileSdk = ApplicationConfig.Sdk.compile

    defaultConfig {
        applicationId = ApplicationConfig.applicationId
        minSdk = ApplicationConfig.Sdk.min
        targetSdk = ApplicationConfig.Sdk.target
        versionCode = appVersionCode
        versionName = "$appVersionNumber [$appVersionCode]"

        testInstrumentationRunner = ApplicationConfig.testInstrumentationRunner

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        maybeCreate("release").apply {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        maybeCreate("debug").apply {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = ApplicationConfig.jvmTarget
    }

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.splashScreen)
    implementation(Dependencies.AndroidX.navigationFragment)
    implementation(Dependencies.AndroidX.navigationUi)
    implementation(Dependencies.AndroidX.roomKtx)
    kapt(Dependencies.AndroidX.roomCompiler)

    implementation(Dependencies.Google.material)
    implementation(Dependencies.Google.gson)
    implementation(Dependencies.Google.hilt)
    kapt(Dependencies.Google.hiltCompiler)
    implementation(Dependencies.Google.exoPlayer)

    implementation(Dependencies.Other.csv)

    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.junitExt)
    androidTestImplementation(Dependencies.Test.espressoCore)
}