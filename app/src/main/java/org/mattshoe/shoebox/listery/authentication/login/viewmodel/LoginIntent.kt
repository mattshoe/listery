package org.mattshoe.shoebox.listery.authentication.login.viewmodel

sealed interface LoginIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data class SignIn(val email: String, val password: String) : LoginIntent
    data object Register : LoginIntent
    data object ResetPassword: LoginIntent
    data object GoogleSignIn : LoginIntent
    data object FacebookSignIn : LoginIntent
}