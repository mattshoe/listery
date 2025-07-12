package org.mattshoe.shoebox.listery.recipe.edit.overview.viewmodel

import org.mattshoe.shoebox.listery.model.EditableField

data class RecipeOverviewState(
    val loading: Boolean,
    val allowSubmit: Boolean = false,
    val name: EditableField<String?> = EditableField(null),
    val website: EditableField<String?> = EditableField(null),
    val hours: EditableField<String?> = EditableField(null),
    val minutes: EditableField<String?> = EditableField(null),
    val calories: EditableField<String> = EditableField(""),
    val notes: EditableField<String?> = EditableField(null),
    val servings: EditableField<Int> = EditableField(1)
)

