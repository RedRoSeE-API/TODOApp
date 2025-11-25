# TODO App

A modern Android TODO application built with Jetpack Compose, Room Database, and MVVM architecture.

## 📋 Table of Contents

## 📋 Table of Contents

- [Idea](#-idea)
- [How It Works](#-how-it-works)
- [Architecture](#-architecture)
- [User Flow](#-user-flow)
- [Getting Started](#-getting-started)
- [Testing](#-testing)
- [Screenshots](#-screenshots)
- [APK Download](#-apk-download)


## 💡 Idea

TODO App is a simple yet powerful task management application that allows users to create, view, edit, and delete tasks. Each task includes a title, description, due date with time, and creation date. The app features sorting capabilities and a clean, intuitive interface built with Material Design 3.

**Key Features:**
- Create tasks with title, description, and due date/time
- View all tasks in a scrollable list
- Edit existing tasks
- Delete an existing task
- Sort tasks by title, creation date, or due date
- Persistent local storage using Room Database
- Material Design 3 UI with dark/light theme support

## 🔧 How It Works

The app uses a local Room database to store all tasks persistently on the device. When you create a task:

1. User fills in the task details (title, description, due date/time)
2. Data is validated and saved to the Room database
3. The UI automatically updates to show the new task
4. Tasks are sorted according to the selected sorting method (default: by title)

All operations are performed locally - no internet connection required.

## 🏗️ Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture pattern with clean architecture principles:
```
src/main/java/com/example/todoapp/
├── ItemDatabase.kt        # Room database setup
├── MainActivity.kt        # App entry point with Compose host

├── converter/             # Type converters for Room
│   └── DateConverter.kt

├── dao/                   # Data Access Objects
│   └── ItemDao.kt

├── dialog/                # Reusable Compose dialogs
│   └── AddItemDialog.kt

├── entity/                # Room entities (data models)
│   └── Item.kt

├── event/                 # UI and domain events
│   └── ItemEvent.kt

├── nav/                   # Navigation graph and routes
│   ├── Navigation.kt
│   └── Screen.kt

├── screens/               # UI screens (Compose)
│   ├── ItemDetailScreen.kt
│   ├── ItemEditScreen.kt
│   └── ItemMainScreen.kt

├── shared/                # Shared state & enums
│   ├── ItemState.kt
│   └── SortTypes.kt

├── theme/                 # Material 3 theme configuration
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt

└── viewmodel/             # ViewModels (business logic)
    └── ItemMainViewModel.kt
```
Structure for Test
```
src/test/java/com/example/todoapp/
├── shared/
│   └── FakeItemDao.kt          # Fake DAO for unit testing
└── viewmodel/
    └── ItemMainViewModelTest.kt

src/androidTest/java/com/example/todoapp/
└── TodoAppUiTest.kt            # Instrumented UI tests (Compose + Espresso)
```

**Technology Stack:**
- **UI:** Jetpack Compose with Material Design 3
- **Database:** Room Database with Type Converters for Date objects
- **Architecture:** MVVM + StateFlow for reactive UI
- **Navigation:** Jetpack Compose Navigation
- **Language:** Kotlin
- **Build System:** Gradle with Kotlin DSL

**Key Components:**

- **Item (Entity):** Represents a TODO task with id, title, description, dueDate, and createdOn
- **ItemDao:** Provides database operations (insert, update, delete, queries)
- **ItemMainViewModel:** Manages app state and handles user events
- **ItemState:** Represents the current UI state
- **ItemEvent:** Sealed interface for all possible user actions

## 👤 User Flow

### Creating a Task

1. User opens the app
2. Clicks the floating action button (+ icon)
3. Fills in the task details:
    - Title (required)
    - Description (optional)
    - Due date and time (optional, via date/time picker)
4. Clicks "Save" to create the task
5. Task appears in the main list

### Viewing a Task

1. User clicks on any task in the list
2. App navigates to the detail screen
3. All task information is displayed (read-only)
4. User can navigate back using the back button

### Editing a Task

1. User clicks on the edit icon
2. Modify any fields
3. Click "Save Changes" to update
4. App returns to the main list with updated task

### Deleting a Task

1. User clicks on the delete icon
2. Task is immediately removed from the list

### Sorting Tasks

1. User clicks the sort button (menu icon)
2. Selects sorting method:
    - By Title (A-Z)
    - By Due Date (earliest first)
    - By Creation Date (newest first)
3. List reorders automatically

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 11 or higher
- Android SDK (minimum API 24, target API 36)
- Git

### Installation Steps

1. **Clone the repository**
```bash
   git clone https://github.com/RedRoSeE-API/MobileApps2025-2301321039.git
   cd MobileApps2025-2301321039
```

2. **Open in Android Studio**
    - Open Android Studio
    - Select "Open an Existing Project"
    - Navigate to the cloned directory
    - Click "OK"

3. **Sync Gradle**
    - Android Studio should automatically sync
    - If not: File → Sync Project with Gradle Files

4. **Build the project**
```bash
   ./gradlew build
```

5. **Run on emulator or device**
    - Click the "Run" button (green triangle)
    - Select your device/emulator
    - App will install and launch

### Configuration

The app uses `.fallbackToDestructiveMigration()` for database migrations during development. For production, proper migration strategies should be implemented.

**Minimum Requirements:**
- Android 7.0 (API 24) or higher
- ~50 MB storage space

## 🧪 Testing

The project includes both unit tests and UI tests.

### Running Unit Tests
```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests ItemMainViewModelTest

# Run with coverage
./gradlew testDebugUnitTest jacocoTestReport
```

**Unit Test Coverage:**
- ViewModel state management
- Event handling
- Date utilities
- Form validation

### Running UI Tests
```bash
# Run all instrumented tests
./gradlew connectedAndroidTest

# Run specific UI test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.todoapp.TodoAppUiTest
```

**UI Test Coverage:**
- Dialog open/close
- Task creation flow
- Task display
- Form validation

### Code Quality

The project uses ktlint for code formatting:
```bash
# Check code style
./gradlew ktlintCheck

# Auto-fix formatting issues
./gradlew ktlintFormat
```

## 📸 Screenshots

### Main Screen
<img src="https://github.com/user-attachments/assets/fb80ab12-f8fb-4828-b673-e33883cb5f2f" width="350"/>

### Add Task Dialog
<img src="https://github.com/user-attachments/assets/aa552665-7767-4f76-9293-63e42b8a4e3e" width="350"/>

### Task Detail View
<img src="https://github.com/user-attachments/assets/5f71abad-3294-4412-b98c-d534837b4f74" width="350"/>

### Edit Task Screen
<img src="https://github.com/user-attachments/assets/80453cd2-4bb3-4784-96c8-68355e762640" width="350"/>


### Sorting Options
*[Add screenshot of sort menu]*

## 📦 APK Download

Download the latest release APK:

**[Download app-release.apk](apk/app-release.apk)**

**Installation:**
1. Download the APK file
2. Enable "Install from Unknown Sources" in your device settings
3. Open the APK file and follow installation prompts

**Version:** 1.0  
**Size:** ~10 MB  
**Minimum Android:** 7.0 (API 24)

## 📝 License

This project is created for educational purposes.

## 👨‍💻 Author

**RedRoSeE-API**  
GitHub: [@RedRoSeE-API](https://github.com/RedRoSeE-API)

---

**Note:** This is a learning project demonstrating modern Android development practices with Jetpack Compose, Room Database, and MVVM architecture.
