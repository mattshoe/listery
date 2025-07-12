package org.mattshoe.shoebox.listery.authentication.profile.viewmodel

import android.net.Uri

sealed interface ProfileIntent {
    data class FirstNameChanged(val value: String) : ProfileIntent
    data class LastNameChanged(val value: String) : ProfileIntent
    data class PhoneNumberChanged(val value: String) : ProfileIntent
    data object EditProfile : ProfileIntent
    data object SaveProfile : ProfileIntent
    data object CancelEdit : ProfileIntent
    data class UploadPhoto(val imageUri: Uri) : ProfileIntent
    data object Logout : ProfileIntent
} 