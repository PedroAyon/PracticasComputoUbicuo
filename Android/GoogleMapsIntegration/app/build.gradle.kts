import org.jetbrains.kotlin.fir.declarations.builder.buildScript
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}


// Cargar propiedades locales
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}


android {
    namespace = "dev.pedroayon.googlemapsintegration"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.pedroayon.googlemapsintegration"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildFeatures {
            buildConfig = true
        }

        // Inyecta la API Key en el AndroidManifest.xml
        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY") ?: ""
        // Permite acceder a la API Key desde BuildConfig
        buildConfigField("String", "MAPS_API_KEY", "\"${localProperties.getProperty("MAPS_API_KEY") ?: ""}\"")
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
    }
}



dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navegación para Compose
    implementation(libs.androidx.navigation.compose)

    // Google Maps Compose
    implementation(libs.maps.compose)

    // Google Play Services Location (opcional para obtener la ubicación real)
    implementation(libs.play.services.location)

    // Accompanist Permissions (opcional para gestionar permisos en Compose)
    implementation(libs.accompanist.permissions)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Retrofit y GSON para consumir la Google Directions API
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}