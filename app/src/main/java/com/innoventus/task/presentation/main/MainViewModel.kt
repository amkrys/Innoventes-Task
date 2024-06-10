package com.innoventus.task.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.innoventus.task.util.SingleLiveEvent
import com.innoventus.task.util.emit

class MainViewModel: ViewModel() {

    private val _buttonClickEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val buttonClickEvent: LiveData<SingleLiveEvent<Unit>> get() = _buttonClickEvent

    fun onButtonClicked() {
        _buttonClickEvent.emit()
    }



}