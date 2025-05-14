plugins {
    id(dependencyCatalog.plugins.convention.android.library.compose.get().pluginId)
}

android {
    namespace = "com.airalo.example.test.roborazzi"
}

dependencies {
    implementation(testDependencyCatalog.bundles.convention.roborazzi)
    testImplementation(dependencyCatalog.bundles.app.compose)
}

dependencyAnalysis {
    issues {
        onUnusedDependencies {
            exclude(
                "androidx.compose.ui:ui-test-manifest",
                "androidx.activity:activity-compose",
                "io.github.takahirom.roborazzi:roborazzi",
                "io.github.takahirom.roborazzi:roborazzi-compose",
                "io.github.takahirom.roborazzi:roborazzi-junit-rule",
                "org.robolectric:robolectric",
                "androidx.test.espresso:espresso-core",
                "androidx.test.ext:junit",
            )
        }
    }
}
