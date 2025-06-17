# InvesProject Android App

## Overview
This is an Android application built with Kotlin and Jetpack Compose, featuring Firebase integration and modern Android development practices.

## Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Minimum SDK**: 24
- **Target SDK**: 35
- **Java Version**: 17

## Key Features
- Material 3 Design
- Firebase Integration
  - Authentication
  - Firestore
  - Storage
  - Analytics
- Media Player Support (ExoPlayer)
- Navigation Component
- Image Loading with Coil

## Dependencies
- Jetpack Compose BOM: 2023.10.01
- Firebase BOM: 32.6.0
- AndroidX Components
  - Activity Compose
  - Lifecycle Components
  - Navigation Compose
- Coil for Image Loading
- Media3 ExoPlayer

## Setup Instructions
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the application

## Build Configuration
The project uses Gradle with Kotlin DSL for build configuration. Key build settings include:
- Compose compiler extension version: 1.5.3
- ProGuard configuration for release builds
- META-INF exclusions for dependency conflicts

## Development Notes
- The project uses experimental Material3 APIs
- Kotlin stdlib version is forced to 1.9.20 for compatibility

## License
[Add your license information here] 