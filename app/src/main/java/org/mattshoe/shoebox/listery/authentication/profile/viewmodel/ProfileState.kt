package org.mattshoe.shoebox.listery.authentication.profile.viewmodel

import org.mattshoe.shoebox.listery.authentication.model.User
import org.mattshoe.shoebox.listery.model.EditableField

data class ProfileState(
    val user: User? = null,
    val firstName: EditableField<String> = EditableField(""),
    val lastName: EditableField<String> = EditableField(""),
    val phoneNumber: EditableField<String> = EditableField(""),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditing: Boolean = false,
    val isPhotoUploading: Boolean = false
) 