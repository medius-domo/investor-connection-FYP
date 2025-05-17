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
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation(compose.html.core)
                
                // Web specific dependencies
                implementation(npm("firebase", "10.7.0"))
            }
        }
    }
}

compose {
    experimental {
        web.application {}
    }
} 