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
```

#### 2. View
The `View` is responsible for displaying the data and handling user interactions. It observes the ViewModel and updates the UI accordingly.

**File: `MainActivity.kt`**
```kotlin
class MainActivity : BaseActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBindingVariables()
        listenToObservers()
    }

    private fun initBindingVariables() = with(binding) {
        lifecycleOwner = this@MainActivity
        viewModel = mainViewModel
    }

    private fun listenToObservers() = lifecycleScope.launch {
        mainViewModel.buttonClickEvent.observeEvent(this@MainActivity) {
            openActivity<FormActivity>()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
```

#### 3. ViewModel
The `ViewModel` handles the business logic and communicates with the Model. It exposes data to the View through LiveData and handles user interactions.

**File: `MainViewModel.kt`**
```kotlin
class MainViewModel: ViewModel() {
    private val _buttonClickEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val buttonClickEvent: LiveData<SingleLiveEvent<Unit>> get() = _buttonClickEvent

    fun onButtonClicked() {
        _buttonClickEvent.emit()
    }
}
```

**File: `FormViewModel.kt`**
```kotlin
class FormViewModel: ViewModel() {
    private val _buttonClickEvent = MutableLiveData<SingleLiveEvent<ClickEvent>>()
    val buttonClickEvent: LiveData<SingleLiveEvent<ClickEvent>> get() = _buttonClickEvent

    val panNumberLiveData = MutableLiveData<String>()
    val dayLiveData = MutableLiveData<String>()
    val monthLiveData = MutableLiveData<String>()
    val yearLiveData = MutableLiveData<String>()

    fun onButtonClick(clickEvent: ClickEvent) {
        _buttonClickEvent.emit(clickEvent)
    }
}
```

### Other components are explained below

#### 1. Event-Handling
The ViewModel handles user interactions and emits events using SingleLiveEvent.

**File: `SingleLiveEvent.kt`**
```kotlin
open class SingleLiveEvent<T>(value: T) {
    var value = value
        private set

    private var isAlreadyHandled = false

    fun isActive(): Boolean = if (isAlreadyHandled) {
        false
    } else {
        isAlreadyHandled = true
        true
    }
}

fun <T> LiveData<SingleLiveEvent<T>>.observeEvent(owner: LifecycleOwner, observer: Observer<T>) =
    observe(owner) {
        if (it.isActive()) {
            observer.onChanged(it.value)
        }
    }

fun MutableLiveData<SingleLiveEvent<Unit>>.emit() = postValue(SingleLiveEvent(Unit))

fun <T> MutableLiveData<SingleLiveEvent<T>>.emit(value: T) = postValue(SingleLiveEvent(value))

fun <T> LiveData<SingleLiveEvent<T>>.value() = value?.value
```

#### 2. Data Binding
The View observes LiveData from the ViewModel and updates the UI accordingly.

**File: `activity_form.xml`**
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="item"
            type="com.innoventus.task.data.uimodel.FormModel" />

        <variable
            name="viewModel"
            type="com.innoventus.task.presentation.form.FormViewModel" />

        <import
            alias="event"
            type="com.innoventus.task.domain.interactor.ClickEvent" />

        <import type="com.innoventus.task.util.StringExtKt" />

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingHorizontal="@dimen/_30sdp"
        android:paddingVertical="@dimen/_16sdp">

        <TextView
            android:id="@+id/tv_header"
            style="@style/HeaderTextAppearance"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/form_header"
            android:textColor="@color/red"
            android:textSize="@dimen/_22sdp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/tv_title"
            style="@style/HeaderTextAppearance"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/_25sdp"
            android:gravity="start"
            android:text="@string/form_title"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/tv_header" />

        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/cl_container"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/_45sdp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/tv_title">

            <TextView
                android:id="@+id/tv_pan_prompt"
                style="@style/PromptTextAppearance"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/pan_number"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/til_pan_number"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/_6sdp"
                android:textColorHint="@color/light_grey"
                app:hintEnabled="false"
                app:layout_constraintTop_toBottomOf="@+id/tv_pan_prompt">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/edt_pan"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="@string/pan_eg"
                    android:maxLength="10"
                    android:text="@={viewModel.panNumberLiveData}" />

            </com.google.android.material.textfield.TextInputLayout>

            <TextView
                android:id="@+id/tv_dob_prompt"
                style="@style/PromptTextAppearance"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/_30sdp"
                android:text="@string/birthdate"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/til_pan_number" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/til_day"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                android:layout_width="@dimen/_55sdp"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/_6sdp"
                android:textColorHint="@color/light_grey"
                app:hintEnabled="false"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/tv_dob_prompt">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/edt_day"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="@string/day_eg"
                    android:imeOptions="actionNext"
                    android:inputType="date"
                    android:maxLength="2"
                    android:text="@={viewModel.dayLiveData}" />

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/til_month"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                android:layout_width="@dimen/_55sdp"
                android:layout_height="wrap_content"
                android:layout_marginStart="@dimen/_18sdp"
                android:layout_marginTop="@dimen/_6sdp"
                android:textColorHint="@color/light_grey"
                app:hintEnabled="false"
                app:layout_constraintStart_toEndOf="@+id/til_day"
                app:layout_constraintTop_toBottomOf="@+id/tv_dob_prompt">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/edt_month"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="@string/month_eg"
                    android:imeOptions="actionNext"
                    android:inputType="date"
                    android:maxLength="2"
                    android:text="@={viewModel.monthLiveData}" />

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/til_year"
                style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginStart="@dimen/_18sdp"
                android:layout_marginTop="@dimen/_6sdp"
                android:textColorHint="@color/light_grey"
                app:hintEnabled="false"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toEndOf="@+id/til_month"
                app:layout_constraintTop_toBottomOf="@+id/tv_dob_prompt">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/edt_year"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:hint="@string/year_eg"
                    android:imeOptions="actionDone"
                    android:inputType="date"
                    android:maxLength="4"
                    android:text="@={viewModel.yearLiveData}" />

            </com.google.android.material.textfield.TextInputLayout>

        </androidx.constraintlayout.widget.ConstraintLayout>

        <TextView
            android:id="@+id/tv_info"
            style="@style/PromptTextAppearance"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginEnd="@dimen/_20sdp"
            android:layout_marginBottom="@dimen/_12sdp"
            android:text="@string/tv_info"
            android:textAllCaps="false"
            android:textStyle="normal"
            app:layout_constraintBottom_toTopOf="@+id/button"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent" />

        <Button
            android:id="@+id/button"
            style="@style/RegularButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="@dimen/_12sdp"
            android:enabled="@{StringExtKt.isValidPan(viewModel.panNumberLiveData) &amp;&amp; StringExtKt.isValidDay(viewModel.dayLiveData) &amp;&amp; StringExtKt.isValidMonth(viewModel.monthLiveData) &amp;&amp; StringExtKt.isValidYear(viewModel.yearLiveData) &amp;&amp; StringExtKt.isValidMonth(viewModel.monthLiveData) &amp;&amp; StringExtKt.isValidYear(viewModel.yearLiveData)}"
            android:onClick="@{()-> viewModel.onButtonClick(event.BUTTON)}"
            android:text="@string/next"
            app:layout_constraintBottom_toTopOf="@+id/tv_no_pan"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent" />

        <TextView
            android:id="@+id/tv_no_pan"
            style="@style/PromptTextAppearance"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/_5sdp"
            android:gravity="center"
            android:onClick="@{()-> viewModel.onButtonClick(event.TEXT)}"
            android:text="@string/tv_no_pan"
            android:textAllCaps="false"
            android:textColor="@color/primary"
            android:textSize="@dimen/_14ssp"
            android:textStyle="normal"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
```

#### 3. Validation
The `Model` includes validation logic to ensure data integrity.

**File: `StringExt.kt`**
```kotlin
fun String?.isValidPan(): Boolean {
    return this?.let {
        val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]")
        val matcher: Matcher = pattern.matcher(this)
        return@let length <= 10 && matcher.matches()
    } == true
}

fun String?.isValidDay(): Boolean {
    return this?.let {
        return@let this.isValid() && this.toInt() in 1..31
    } == true
}

fun String?.isValidMonth(): Boolean {
    return this?.let {
        return@let this.isValid() && this.toInt() in 1 .. 12
    } == true
}

fun String?.isValidYear(): Boolean {
    return this?.let {
        return@let this.isValid() && this.toInt() in 1990..2024
    } == true
}
```

## Summary
**MVVM Pattern**: Separates concerns into Model, View, and ViewModel.
**SingleLiveEvent**: Ensures events are only handled once.
**Data Binding**: Connects the ViewModel with the UI components.
**Validation**: Ensures data integrity in the Model.
This architecture makes the code more modular, testable, and maintainable.
