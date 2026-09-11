plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvm("desktop").withJava()

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.slf4j.api)
            implementation(libs.gson)
            implementation(libs.json.jvm)
            implementation(libs.jlatexmath)
            implementation(libs.pdfbox)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime:2.9.4")
            implementation(libs.savedstate)
            implementation(libs.annotation)
            implementation(libs.collection)
            implementation("androidx.datastore:datastore-preferences:1.1.7")
        }
    }
}
