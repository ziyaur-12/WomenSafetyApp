# 🛡️ Women Safety App

A feature-rich Android application built with **Kotlin** and **Firebase** to empower women with real-time safety tools — including an SOS alert system, emergency contacts management, live location tracking, safe zone mapping, and a fake call generator.

---

## 📱 Features

### 🆘 SOS Alert
- One-tap SOS button on the home screen
- Automatically fetches the user's current GPS location
- Sends an emergency SMS with a **Google Maps link** to all saved contacts
- Also triggered by **shaking the phone** (shake detection via accelerometer)

### 📍 Live Location Tracking
- Foreground **LocationService** continuously tracks the user's position
- Location shared via Firebase Realtime Database
- Works in the background even when the app is minimized

### 🗺️ Safe Zone Map
- Integrated **Google Maps** view
- Shows the user's current location with a live marker
- Displays nearby **Police Stations** and **Women Help Centers** as map markers
- Zoom controls and My Location button enabled

### 👥 Emergency Contacts
- Add and manage emergency contacts (name + phone number)
- Contacts stored securely in **Firebase Realtime Database** per user
- Long-press a contact to delete it
- Contacts are used automatically when SOS is triggered

### 📞 Fake Call
- Simulate an incoming call to escape uncomfortable situations
- 5-second countdown before the fake call appears
- Plays the device's default ringtone and vibrates
- Accept (shows "Connected") or Reject (exits) options

### 🔐 Authentication
- Email/password login via **Firebase Authentication**
- All data is scoped per authenticated user
- Auto-redirects to login if the session has expired

---

## 🏗️ Architecture

This project follows the **MVVM (Model-View-ViewModel)** pattern:

```
app/src/main/java/com/example/womensafetyapp/
├── model/          # Data models (e.g., Contact)
├── repository/     # Firebase data access layer
├── services/       # LocationService (foreground service)
├── ui/             # Activities (Login, Main, Maps, Contacts, FakeCall)
├── utils/          # ShakeDetector, SOSHelper
└── viewmodel/      # ViewModels for MVVM
```

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| **Kotlin** | Primary language |
| **Java** | Legacy activity stubs |
| **Firebase Auth** | User authentication |
| **Firebase Realtime Database** | Emergency contacts & location storage |
| **Firebase Firestore** | Extended data storage |
| **Google Maps SDK** | Map display & location markers |
| **Google Play Services Location** | GPS / fused location provider |
| **AndroidX Lifecycle (MVVM)** | ViewModel & LiveData |
| **Material Design 3** | UI components |
| **View Binding** | Type-safe view access |

---

## 📋 Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or newer
- **Android SDK** 34 (compile & target)
- **Min SDK**: 24 (Android 7.0 Nougat)
- A **Firebase project** with:
  - Authentication (Email/Password) enabled
  - Realtime Database enabled
  - `google-services.json` placed in the `app/` directory
- A **Google Maps API key** (set in `AndroidManifest.xml`)

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/your-username/WomenSafetyApp.git
cd WomenSafetyApp
```

### 2. Firebase Setup
1. Go to [Firebase Console](https://console.firebase.google.com/) and create a new project
2. Add an Android app with package name `com.example.womensafetyapp`
3. Download `google-services.json` and place it in `app/google-services.json`
4. Enable **Email/Password** sign-in under Authentication
5. Enable **Realtime Database** and set rules to allow authenticated reads/writes:
   ```json
   {
     "rules": {
       "contacts": {
         "$uid": {
           ".read": "$uid === auth.uid",
           ".write": "$uid === auth.uid"
         }
       }
     }
   }
   ```

### 3. Google Maps API Key
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Enable the **Maps SDK for Android**
3. Create an API key and replace the value in `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY_HERE" />
   ```

### 4. Build & Run
Open the project in **Android Studio** and click ▶ **Run**, or use:
```bash
./gradlew assembleDebug
```
The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔐 Permissions

The app requests the following Android permissions:

| Permission | Purpose |
|---|---|
| `ACCESS_FINE_LOCATION` | Precise GPS location for SOS & map |
| `ACCESS_COARSE_LOCATION` | Fallback coarse location |
| `SEND_SMS` | Sending SOS messages to contacts |
| `INTERNET` | Firebase & Maps communication |
| `VIBRATE` | Fake call vibration |
| `FOREGROUND_SERVICE` | Background location tracking |
| `FOREGROUND_SERVICE_LOCATION` | Location-type foreground service |
| `POST_NOTIFICATIONS` | Service notifications (Android 13+) |

---

## 📂 Project Structure

```
WomenSafetyApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/womensafetyapp/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── services/
│   │   │   │   └── LocationService.kt
│   │   │   ├── ui/
│   │   │   │   ├── LoginActivity.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── ContactsActivity.kt
│   │   │   │   ├── MapsActivity.java
│   │   │   │   └── FakeCallActivity.java
│   │   │   ├── utils/
│   │   │   │   └── ShakeDetector.kt
│   │   │   └── viewmodel/
│   │   ├── res/                  # Layouts, drawables, strings
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── google-services.json
├── build.gradle
├── gradle.properties
└── settings.gradle
```

---

## ✅ Build Status

The project builds successfully with Gradle:

```
BUILD SUCCESSFUL in 4m 27s
39 actionable tasks completed
```

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

## 🙏 Acknowledgements

- [Firebase](https://firebase.google.com/) — Backend as a Service
- [Google Maps Platform](https://developers.google.com/maps) — Maps & Location
- [Material Design](https://material.io/) — UI Components
- Built with ❤️ to make the world safer for women
