package org.mattshoe.shoebox.listery.authentication.register.viewmodel

sealed interface UserIntent {
    data class EmailUpdated(val value: String): UserIntent
    data class PasswordUpdated(val value: String): UserIntent
    data class NameUpdated(val value: String): UserIntent
    data class PhoneNumberUpdated(val value: String): UserIntent
    data class Register(val state: State): UserIntent
}