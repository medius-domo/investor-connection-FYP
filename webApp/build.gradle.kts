plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                devServer?.port = 8080 // You can change this port if needed
                cssSupport { enabled.set(true) }
            }
            binaries.executable()
        }
    }
    
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(compose.html.svg)
                
                // Web specific dependencies
                implementation(npm("firebase", "10.7.0"))
                
                // Add missing dependencies
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-js:1.0.0-pre.682")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-extensions:1.0.1-pre.682")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.682")
            }
        }
    }
}

compose {
    experimental {
        web.application {}
    }
} 