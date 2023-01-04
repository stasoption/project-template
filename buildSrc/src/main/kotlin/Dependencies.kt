object AppVersions {
    const val compileSdk = 31
    const val minSdk = 22
    const val targetSdk = 31
    const val detekt = "1.15.0"
    const val jacocoVersion = "0.8.2"
}

object Plugins {
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val javaLibrary = "java-library"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val navigationSafeArgsKotlin = "androidx.navigation.safeargs.kotlin"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val gradleVersions = "com.github.ben-manes.versions"
    const val checkstyle = "checkstyle"
    const val jacoco = "com.vanniktech.android.junit.jacoco"
    const val crashlyticsPlugin = "com.google.firebase.crashlytics"
}

object ClassPaths {
    object Versions {
        const val gradle = "7.1.2"
        const val kotlin = "1.7.0"
        const val jacoco = "0.16.0"
        const val navigation = "2.3.2"
        const val detekt = "1.15.0"
        const val gradleVersions = "0.28.0"
    }

    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val jacoco = "com.vanniktech:gradle-android-junit-jacoco-plugin:${Versions.jacoco}"
    const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersions}"
}

object Modules {
    const val app = ":app"
    const val core = ":core"
    const val features = ":features"
}

object KotlinDependencies {
    object Versions {
        const val kotlinVersion = "1.4.31"
        const val kotlinCoroutines = "1.4.2"
    }

    const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
}

object AndroidXDependencies {
    object Versions {
        const val multidex = "2.0.1"
        const val core = "1.6.0"
        const val appCompat = "1.2.0"
        const val fragments = "1.3.0"
        const val lifecycle = "2.3.0"
        const val arch = "2.1.0"
        const val customTabs = "1.3.0"
        const val material = "1.5.0-alpha02"
        const val recyclerView = "1.1.0"
        const val swipeRefresh = "1.1.0"
        const val constraintLayout = "2.0.4"
        const val navigation = "2.3.2"
        const val exifInformation = "1.3.2"
        const val preferences = "1.1.1"
        const val flexbox = "3.0.0"
    }

    const val multidexLib = "androidx.multidex:multidex:${Versions.multidex}"
    const val coreLib = "androidx.core:core-ktx:${Versions.core}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val fragments = "androidx.fragment:fragment-ktx:${Versions.fragments}"
    const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val common = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val test = "androidx.arch.core:core-testing:${Versions.arch}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val customTabsLib = "androidx.browser:browser:${Versions.customTabs}"
    const val exifInformation = "androidx.exifinterface:exifinterface:${Versions.exifInformation}"
    const val preferences = "androidx.preference:preference:${Versions.preferences}"
    const val flexbox = "com.google.android.flexbox:flexbox:${Versions.flexbox}"
}

object DiDependencies {
    object Versions {
        const val koin = "2.2.2"
    }

    const val koin = "io.insert-koin:koin-android:${Versions.koin}"
    const val fragment = "io.insert-koin:koin-androidx-fragment:${Versions.koin}"
    const val viewModel = "io.insert-koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koinScope = "io.insert-koin:koin-androidx-scope:${Versions.koin}"
    const val koinWorkManager = "io.insert-koin:koin-androidx-workmanager:${Versions.koin}"
    const val koinTest = "io.insert-koin:koin-test:${Versions.koin}"
}

object JsonDependencies {
    object Versions {
        const val moshi = "1.13.0"
    }

    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiCompiler = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val moshiAdapters = "com.squareup.moshi:moshi-adapters:${Versions.moshi}"
}

object NetworkDependencies {
    object Versions {
        const val retrofit = "2.8.1"
        const val okHttp = "4.9.1"
    }

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
}

object TestDependencies {
    object Versions {
        const val junit = "4.12"
        const val mockito = "2.22.0"
    }

    const val junit = "junit:junit:${Versions.junit}"
    const val mockito = "org.mockito:mockito-core:${Versions.mockito}"
}

object MiscDependencies {
    object Versions {
        const val timber = "4.7.1"
        const val adapterDelegates = "4.3.0"
        const val viewBindingPropertyDelegate = "1.4.3"
        const val ktLint = "0.40.0"
        const val checkstyle = "8.38"
        const val shimmer = "0.5.0"
        const val joda = "2.10.10"
        const val keyboardvisibilityevent = "3.0.0-RC3"
        const val inputMask = "6.1.0"
    }

    const val timberLib = "com.jakewharton.timber:timber:${Versions.timber}"
    const val joda = "joda-time:joda-time:${Versions.joda}"
    const val adapterDelegates =
        "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:${Versions.adapterDelegates}"
    const val viewBindingPropertyDelegate =
        "com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Versions.viewBindingPropertyDelegate}"

    const val ktlint = "com.pinterest:ktlint:${Versions.ktLint}"
    const val checkstyle = "com.puppycrawl.tools:checkstyle:${Versions.checkstyle}"
    const val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"
    const val keyboardvisibilityevent = "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:${Versions.keyboardvisibilityevent}"
    const val inputMask = "com.github.RedMadRobot:input-mask-android:${Versions.inputMask}"
}

object GlideDependencies {
    object Versions {
        const val glide = "4.11.0"
        const val transformations = "4.3.0"
    }
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val okhttp3Integration = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
    const val transformations = "jp.wasabeef:glide-transformations:${Versions.transformations}"
}

object Exoplayer {
    object Versions {
        const val exoplayer = "2.17.1"
    }
    const val exoplayer = "com.google.android.exoplayer:exoplayer:${Versions.exoplayer}"
}

object ChartsDependencies {
    object Versions {
        const val chart = "v3.1.0"
    }
    const val androidChart = "com.github.PhilJay:MPAndroidChart:${Versions.chart}"
}

