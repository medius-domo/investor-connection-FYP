plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                devServer?.port = 8080
                cssSupport { enabled.set(true) }
            }
            binaries.executable()
        }
    }
    
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.html.core)
                implementation(compose.html.svg)
                implementation(project(":shared"))
                
                // Web specific dependencies
                implementation(npm("firebase", "10.7.0"))
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