package org.mattshoe.shoebox.listery.model

data class EditableField<T>(
    val value: T,
    val enabled: Boolean = true,
    val error: String? = null
)