# BookEase

BookEase is a native Android appointment management application developed using Kotlin, Jetpack Compose and Firebase.

The application allows customers to browse available services, book appointments, manage their bookings and cancel appointments. Administrators can manage services through role-based access control.

## Features

### Customer

- User registration and login
- Firebase Authentication
- Browse available services
- View service details
- Select appointment date and time
- Duplicate booking prevention
- View personal booking history
- Cancel confirmed bookings

### Administrator

- Role-based administrator access
- Add new services
- Edit existing services
- Enable or disable services
- Manage service information

## Technologies

- Kotlin
- Android Studio
- Jetpack Compose
- Navigation Compose
- Firebase Authentication
- Cloud Firestore
- Firestore Security Rules
- Git / GitHub

## Architecture

The project uses a repository-based structure to separate the UI layer from Firebase data access.

Jetpack Compose UI
        |
        v
Navigation Compose
        |
        v
Repository Layer
        |
        v
Firebase SDK
        |
        +--- Firebase Authentication
        |
        +--- Cloud Firestore
        |
        +--- Firestore Security Rules
