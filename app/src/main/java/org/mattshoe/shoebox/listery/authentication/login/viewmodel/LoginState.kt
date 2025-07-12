package org.mattshoe.shoebox.listery.authentication.login.viewmodel

import org.mattshoe.shoebox.listery.model.EditableField

data class LoginState(
    val email: EditableField<String> = EditableField(""),
    val password: EditableField<String> = EditableField(""),
    val allowSignIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)