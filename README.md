# InvesProject - Connect Investors with Innovators

A cross-platform application built with Kotlin Compose Multiplatform that connects investors with innovators. The application runs on both Android and Web platforms, sharing business logic and UI components.

## 🛠 Tech Stack

- **Kotlin Compose Multiplatform**: For cross-platform UI development
- **Firebase**: Authentication and Firestore for data storage
- **Material 3**: Modern UI design system
- **MVVM Architecture**: Clean and maintainable codebase

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 17 or later
- Kotlin 1.9.20 or later
- Firebase project setup (see Firebase Setup section)

### Building the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/invesproject.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files and wait for the build to complete

### Running on Android

1. Select the `androidApp` configuration
2. Choose your target device (emulator or physical device)
3. Click the Run button (▶️) or press Shift + F10

### Running on Web

1. Open terminal in the project root
2. Run the following command:
   ```bash
   ./gradlew :webApp:jsBrowserDevelopmentRun
   ```
3. Open your browser and navigate to `http://localhost:8080`

## 🔥 Firebase Setup

1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Add Android and Web apps to your project
3. Download `google-services.json` and place it in the `androidApp` directory
4. Add your Firebase web configuration to `webApp/src/jsMain/resources/firebase-config.js`

## 📱 Features

- **Authentication**: Email/password sign-up and sign-in
- **Role-based Access**: Separate flows for investors and innovators
- **Idea Submission**: Submit and manage business proposals
- **Investor Dashboard**: Browse and express interest in ideas
- **Communication**: In-app messaging between investors and innovators

## 🏗 Project Structure

```
project-root/
├── androidApp/          # Android-specific implementation
├── webApp/             # Web-specific implementation
├── shared/             # Shared code between platforms
│   ├── data/          # Repositories and data sources
│   ├── domain/        # Business logic and models
│   ├── presentation/  # ViewModels and UI state
│   └── ui/            # Shared Compose UI components
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details 