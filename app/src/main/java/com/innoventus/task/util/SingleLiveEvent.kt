package com.innoventus.task.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

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
