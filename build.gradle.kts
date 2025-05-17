plugins {
    // Kotlin Multiplatform
    kotlin("multiplatform") version "1.9.20" apply false
    kotlin("plugin.serialization") version "1.9.20" apply false
    
    // Android
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    
    // Compose
    id("org.jetbrains.compose") version "1.5.10" apply false
    
    // Firebase
    id("com.google.gms.google-services") version "4.4.0" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
} 