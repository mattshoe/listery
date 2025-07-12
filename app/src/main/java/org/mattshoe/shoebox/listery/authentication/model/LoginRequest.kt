package org.mattshoe.shoebox.listery.authentication.model

sealed interface LoginRequest {
    data class Email(val email: String, val password: String): LoginRequest
    data object GoogleSignIn: LoginRequest
    data object FacebookSignIn: LoginRequest
}