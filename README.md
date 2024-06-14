# Innoventes Task

This project follows the MVVM (Model-View-ViewModel) architecture pattern, which is a common design pattern used in Android development to separate concerns and make the code more maintainable and testable.

## Architecture Overview

### Model-View-ViewModel (MVVM) Architecture

#### 1. Model
The `Model` represents the data layer of the application. It includes data classes and business logic.

**File: `FormModel.kt`**
```kotlin
data class FormModel(
    var panNumber: String = "",
    var day: String = "",
    var month: String = "",
    var year: String = ""
) {
    fun isValid(): ObservableBoolean {
        return ObservableBoolean(panNumber.isValidPan() && day.isValidDay() && month.isValidMonth() && year.isValidYear())
    }
}

#### 2. View
The `View` is responsible for displaying the data and handling user interactions. It observes the ViewModel and updates the UI accordingly.
