package org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel

import org.mattshoe.shoebox.listery.model.EditableField

data class State(
    val loading: Boolean,
    val pageError: String? = null,
    val name: String? = null,
    val website: EditableField<String?> = EditableField(null),
    val hours: EditableField<String?> = EditableField(null),
    val minutes: EditableField<String?> = EditableField(null),
    val calories: EditableField<String?> = EditableField(null),
    val notes: EditableField<String?> = EditableField(null),
)

