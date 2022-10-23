object Dependencies {

    object Core {
        const val buildGradle = "com.android.tools.build:gradle:${Versions.buildGradle}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinGradle}"
        const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.ktxCore}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    }

    object Google {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val gson = "com.google.code.gson:gson:${Versions.gson}"
        const val hilt = "com.google.dagger:hilt-android:${Plugins.Versions.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:${Plugins.Versions.hilt}"
        const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Versions.exoPlayer}"
    }

    object Other {
        const val csv = "com.github.doyaaaaaken:kotlin-csv-jvm:${Versions.csv}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Versions {
        // Core
        const val buildGradle = "7.3.1"
        const val kotlinGradle = "1.6.20"
        // AndroidX
        const val ktxCore = "1.9.0"
        const val appcompat = "1.5.1"
        const val splashScreen = "1.0.0"
        const val navigation = "2.6.0-alpha02"
        const val room = "2.4.3"
        // Google
        const val material = "1.7.0"
        const val gson = "2.9.0"
        const val exoPlayer = "2.18.1"
        // Other
        const val csv = "1.3.0"
        // Test
        const val junit = "4.13.2"
        const val junitExt = "1.1.3"
        const val espresso = "3.4.0"
    }

}