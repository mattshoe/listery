package org.mattshoe.shoebox.listery.authentication.register.viewmodel

import org.mattshoe.shoebox.listery.model.EditableField

data class State(
    val loading: Boolean = false,
    val allowSubmit: Boolean = false,
    val error: String? = null,
    val email: EditableField<String> = EditableField(""),
    val password: EditableField<String> = EditableField(""),
    val name: EditableField<String?> = EditableField(""),
    val phoneNumber: EditableField<String?> = EditableField(""),
)