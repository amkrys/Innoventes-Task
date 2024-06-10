package com.innoventus.task.data.uimodel

import androidx.databinding.ObservableBoolean
import com.innoventus.task.util.isValidDay
import com.innoventus.task.util.isValidMonth
import com.innoventus.task.util.isValidPan
import com.innoventus.task.util.isValidYear

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