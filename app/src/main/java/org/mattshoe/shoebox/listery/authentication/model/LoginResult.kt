package org.mattshoe.shoebox.listery.authentication.model

sealed interface LoginResult {
    data class Success(val user: User): LoginResult
    data class Error(val message: String): LoginResult
}