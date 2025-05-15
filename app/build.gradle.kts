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

dependencies {
    implementation(dependencyCatalog.appcompat)
    implementation(dependencyCatalog.androidx.splashScreen)
    implementation(projects.offer)
    implementation(projects.command)
    implementation(dependencyCatalog.koin.android)
    implementation(dependencyCatalog.koin.androidx.compose)
    implementation(dependencyCatalog.bundles.app.compose)
}
