plugins {
    alias(buildDependencyCatalog.plugins.android.application)
    alias(buildDependencyCatalog.plugins.kotlin.android)
    alias(buildDependencyCatalog.plugins.kotlin.compose)
    alias(buildDependencyCatalog.plugins.serialization)
    alias(testDependencyCatalog.plugins.roborazzi)
}

android {
    namespace = "com.airalo.sample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.airalo.sample"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

roborazzi {
    // Directory for reference images
    outputDir.set(file("${projectDir.absolutePath}/screenshots"))
}

dependencies {
    implementation(dependencyCatalog.androidx.core.ktx)
    implementation(dependencyCatalog.androidx.lifecycle.runtime.ktx)
    implementation(dependencyCatalog.androidx.activity.compose)
    implementation(platform(dependencyCatalog.androidx.compose.bom))
    implementation(dependencyCatalog.androidx.ui)
    implementation(dependencyCatalog.androidx.ui.graphics)
    implementation(dependencyCatalog.androidx.ui.tooling.preview)
    implementation(dependencyCatalog.androidx.material3)
    implementation(platform(dependencyCatalog.koin.bom))
    implementation(dependencyCatalog.koin.android)
    implementation(dependencyCatalog.koin.androidx.compose)
    implementation(dependencyCatalog.coil.compose)
    implementation(dependencyCatalog.coil)
    implementation(dependencyCatalog.coil.network)
    implementation(dependencyCatalog.retrofit)
    implementation(dependencyCatalog.serialisation.core)
    implementation(dependencyCatalog.serialisation.json)
    implementation(dependencyCatalog.retrofit.kotlin.serialization)

    debugImplementation(dependencyCatalog.androidx.ui.tooling)
    debugImplementation(dependencyCatalog.androidx.ui.test.manifest)

    testImplementation(testDependencyCatalog.cashapp.turbine)
    testImplementation(testDependencyCatalog.bundles.junit5)
    testImplementation(testDependencyCatalog.bundles.koin)
    testImplementation(testDependencyCatalog.bundles.roborazzi)
    testImplementation(testDependencyCatalog.bundles.compose)
    testImplementation(testDependencyCatalog.mockwebserver)
    testImplementation(testDependencyCatalog.anitbytes.assertions)
    testImplementation(testDependencyCatalog.kfixture)
    testImplementation(testDependencyCatalog.mockk.core)
    testImplementation(testDependencyCatalog.coil)


    androidTestImplementation(testDependencyCatalog.compose.ui.junit)
    androidTestImplementation(testDependencyCatalog.androidx.junit)
    androidTestImplementation(testDependencyCatalog.espresso.core)
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()
}
