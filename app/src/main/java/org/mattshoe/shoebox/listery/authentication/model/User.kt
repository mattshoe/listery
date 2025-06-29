package org.mattshoe.shoebox.listery.authentication.model

data class User(
    val id: String,
    val name: String?,
    val email: String?,
    val photoUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val phoneNumber: String? = null
)