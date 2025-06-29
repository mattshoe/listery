package org.mattshoe.shoebox.listery.authentication.util

import android.util.Patterns


fun String.isValidPassword(): Boolean {
    return length >= 8 && any { !it.isLetter() }
}

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}