package org.mattshoe.shoebox.listery.authentication.model

sealed interface AuthRequest {
    data class Email(val email: String, val password: String): AuthRequest
    data class Register(val email: String, val password: String): AuthRequest
    data object GoogleSignIn: AuthRequest
    data object FacebookSignIn: AuthRequest
    data class ResetPassword(val email: String)
}