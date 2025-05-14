plugins {
    id(dependencyCatalog.plugins.convention.android.appliction.get().pluginId)
}

android {
    namespace = "com.airalo.example"
    defaultConfig {
        applicationId = "com.airalo.example"
        versionCode = 1
        versionName = "1.0"
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
    implementation(dependencyCatalog.coil.core)
    implementation(dependencyCatalog.coil.network)

    debugImplementation(dependencyCatalog.androidx.ui.tooling)
    debugImplementation(dependencyCatalog.androidx.ui.test.manifest)

    testImplementation(testDependencyCatalog.cashapp.turbine)
    testImplementation(testDependencyCatalog.bundles.convention.roborazzi)
    testImplementation(testDependencyCatalog.anitbytes.assertions)
    testImplementation(testDependencyCatalog.kfixture)
    testImplementation(testDependencyCatalog.mockk.core)
    testImplementation(testDependencyCatalog.coil)

    androidTestImplementation(testDependencyCatalog.compose.ui.junit)
    androidTestImplementation(testDependencyCatalog.androidx.junit)
    androidTestImplementation(testDependencyCatalog.espresso.core)
}
