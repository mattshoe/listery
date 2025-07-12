package org.mattshoe.shoebox.listery.recipe.edit.ingredient.viewmodel

import org.mattshoe.shoebox.listery.model.EditableField

data class State(
    val loading: Boolean = false,
    val allowSubmit: Boolean = false,
    val name: EditableField<String?> = EditableField(null),
    val quantity: EditableField<String> = EditableField("1.0"),
    val unit: EditableField<String> = EditableField("whole"),
    val calories: EditableField<String> = EditableField("")
)