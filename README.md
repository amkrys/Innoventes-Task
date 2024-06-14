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



