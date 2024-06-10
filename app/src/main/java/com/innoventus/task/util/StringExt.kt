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

fun String.isValidPan(): Boolean {
    val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]")
    val matcher: Matcher = pattern.matcher(this)
    return length <= 10 && matcher.matches()
}