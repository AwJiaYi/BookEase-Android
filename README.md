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

```text
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

## Project Structure

```text
com.jiayi.bookease

├── data
│   ├── AuthRepository
│   ├── ServiceRepository
│   └── AppointmentRepository
│
├── model
│   ├── User
│   ├── Service
│   └── Appointment
│
├── navigation
│   └── AppNavigation
│
└── ui
    ├── LoginScreen
    ├── RegisterScreen
    ├── HomeScreen
    ├── ServiceListScreen
    ├── ServiceDetailScreen
    ├── BookingScreen
    ├── MyBookingsScreen
    ├── AdminServiceScreen
    ├── AddServiceScreen
    └── EditServiceScreen

## Business Rules

- BookEase includes several business validation rules:

- Users must authenticate before accessing booking features.
- Customers may only access their own appointments.
- Duplicate confirmed bookings for the same service, date and time are prevented.
- Cancelled appointments remain stored for booking history.
- Only administrators can create or modify services.
- New public registrations receive the customer role by default.

## Firebase Data Model
### users
```text
uid
name
email
role
### services
```text
id
name
description
duration
price
active
### appointments
```text
id
userId
serviceId
serviceName
date
time
price
status
createdAt

## Screenshots

Screenshots of the application workflow will be added here.

## Software Development Lifecycle

The project was developed using an iterative SDLC approach:

1. Requirements identification
2. Data model design
3. UI and navigation design
4. Authentication implementation
5. Firestore integration
6. Business rule implementation
7. Role-based access control
8. Functional testing
9. UI refinement

## Future Improvements
- Real-time available time slots
- Firestore transaction-based booking
- Push notifications
- Appointment rescheduling
- Administrator booking dashboard
- Automated testing

## Author
Aw Jia Yi
Bachelor of Software Engineering (Honours)
