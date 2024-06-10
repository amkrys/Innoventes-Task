package com.innoventus.task.util

import java.util.regex.Matcher
import java.util.regex.Pattern

fun String?.stringNotNull(): Boolean {
    return this != null
}

fun String.stringNotEmpty(): Boolean {
    return this.isNotEmpty()
}

fun String.isValid(): Boolean {
    return stringNotNull() && trim().stringNotEmpty()
}

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