# EV Battery Rental App (Abdroid)- Token-Based Electric Vehicle Inspection

<img src="motion_logo.png" width="120">

## Introduction
Welcome to the EV Battery Rental App, a revolutionary solution for electric vehicle owners and TIC (Testing, Inspection, and Certification) companies. This mobile application offers a token-based battery rental system, enabling seamless monthly battery rental payments and vehicle inspection tracking. This readme provides an overview of the app, its key features, and the technologies used in its development.

## About the Project
The EV Battery Rental App follows the MVVM (Model-View-ViewModel) architecture pattern. In this design, the app’s interface components observe real-time data updates from the view model. The view model accesses the necessary data from the data source layer, which includes the model and repository. The data source layer integrates API responses and user data, facilitating a smooth experience for managing battery rentals, inspection schedules, and payment processing.

### Key Features:
- **Token-Based Battery Rental System**: Monthly payments for battery rentals using a token system.
- **Vehicle Inspection Management**: Track inspection reports and schedule services.
- **Battery Health Monitoring**: View battery status, usage statistics, and maintenance needs.

## Mobile Tech Stack
The EV Battery Rental App is built using the following technologies:

- **Android Studio**: The official IDE for Android app development.
- **Kotlin Programming Language**: A modern programming language for developing Android apps.
- **Viewpager**: A component for implementing swipeable views in the app.
- **Datastore**: A data storage solution for managing user preferences, token balances, and app settings.
- **Camerax**: Capture and upload images for vehicle inspection reports.
- **Jetpack Library**:
  - **Lifecycle**: Manage lifecycle-aware components for ensuring app stability.
  - **Navigation**: Handle seamless navigation across app features like payments, inspections, and reports.
  - **UI**: Provides polished user interface components for an intuitive experience.
  - **Data**: Leverage LiveData and ViewModel to manage data between the app layers.
- **Retrofit2**: A type-safe HTTP client for making secure API requests for rental payments and inspections.
- **Glide**: Load and display images of vehicles and battery status in the app.
- **Lottie**: Add smooth animations for improved user experience and interactions.

## API Mobile Documentation
For detailed information about the EV Battery Rental App’s API, please refer to the following documentation:
[API Mobile Documentation](https://ev-battery-rental.example.com/api-docs/)

## Project Installation Guide
To install and run the EV Battery Rental App on your local machine, follow these steps:

1. Clone the project repository from GitHub:
    ```bash
    git clone https://github.com/ev-battery-rental/app.git](https://github.com/Elvora-Battery/elvora-android.git
    ```
2. Open the project in Android Studio.

3. Build and run the app on an emulator or physical device.

## Figma Design
To view the user interface design of the app, visit the Figma link:
[Design Figma](https://www.figma.com/design/06SKO0CKh4mTS8fNjEpuZ0/Elvora?node-id=26-137&node-type=canvas&t=oZ88mviCKCZ7HFMT-0)

For more detailed instructions and troubleshooting tips, please consult the project's GitHub repository:
[GitHub Repository](https://github.com/Elvora-Battery)
