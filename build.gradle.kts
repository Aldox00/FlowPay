// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // 🎯 AGREGA ESTA LÍNEA AQUÍ (Le dice a Gradle de dónde descargar el plugin)
    id("com.google.gms.google-services") version "4.4.2" apply false
}