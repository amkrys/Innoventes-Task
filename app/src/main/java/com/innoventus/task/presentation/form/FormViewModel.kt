package com.innoventus.task.presentation.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.innoventus.task.domain.interactor.ClickEvent
import com.innoventus.task.util.SingleLiveEvent
import com.innoventus.task.util.emit
import com.innoventus.task.util.isValidDate
import com.innoventus.task.util.isValidDay
import com.innoventus.task.util.isValidMonth
import com.innoventus.task.util.isValidPan
import com.innoventus.task.util.isValidYear

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

    fun isValid(pan: String?, day: String?, month: String?, year: String?): Boolean {
        return pan.isValidPan() && day.isValidDay()
                && month.isValidMonth() && year.isValidYear()
                && "$day/$month/$year".isValidDate()
    }

}