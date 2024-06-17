package com.innoventus.task.data.uimodel

import androidx.lifecycle.MutableLiveData
import com.innoventus.task.util.isValidDate
import com.innoventus.task.util.isValidDay
import com.innoventus.task.util.isValidMonth
import com.innoventus.task.util.isValidPan
import com.innoventus.task.util.isValidYear

data class FormModel(
    val panNumberLiveData: MutableLiveData<String> = MutableLiveData<String>(),
    val dayLiveData: MutableLiveData<String> = MutableLiveData<String>(),
    val monthLiveData: MutableLiveData<String> = MutableLiveData<String>(),
    val yearLiveData: MutableLiveData<String> = MutableLiveData<String>(),
) {

    fun isValid(pan: String?, day: String?, month: String?, year: String?): Boolean {
        return pan.isValidPan() && day.isValidDay()
                && month.isValidMonth() && year.isValidYear()
                && "$day/$month/$year".isValidDate()
    }

}