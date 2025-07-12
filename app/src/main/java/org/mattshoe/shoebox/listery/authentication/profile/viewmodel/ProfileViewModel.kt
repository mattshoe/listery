package org.mattshoe.shoebox.listery.authentication.profile.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.authentication.data.SessionRepository
import org.mattshoe.shoebox.listery.authentication.profile.usecase.LogoutUseCase
import org.mattshoe.shoebox.listery.authentication.profile.usecase.UpdateProfileUseCase
import org.mattshoe.shoebox.listery.authentication.profile.usecase.UploadProfilePhotoUseCase
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Routes
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val logoutUseCase: LogoutUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadProfilePhotoUseCase: UploadProfilePhotoUseCase,
    private val navigationProvider: NavigationProvider
) : ListeryViewModel<ProfileState, ProfileIntent>(ProfileState()) {

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = sessionRepository.currentUser
        if (currentUser != null) {
            val nameParts = currentUser.name?.split(" ") ?: listOf()
            val firstName = nameParts.firstOrNull() ?: ""
            val lastName = nameParts.drop(1).joinToString(" ")
            
            updateState {
                it.copy(
                    user = currentUser,
                    firstName = EditableField(firstName),
                    lastName = EditableField(lastName),
                    phoneNumber = EditableField(currentUser.phoneNumber ?: "")
                )
            }
        }
    }

    override fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.FirstNameChanged -> handleFirstNameChanged(intent)
            is ProfileIntent.LastNameChanged -> handleLastNameChanged(intent)
            is ProfileIntent.PhoneNumberChanged -> handlePhoneNumberChanged(intent)
            is ProfileIntent.EditProfile -> handleEditProfile()
            is ProfileIntent.SaveProfile -> handleSaveProfile()
            is ProfileIntent.CancelEdit -> handleCancelEdit()
            is ProfileIntent.UploadPhoto -> handleUploadPhoto(intent)
            is ProfileIntent.Logout -> handleLogout()
        }
    }

    private fun handleFirstNameChanged(intent: ProfileIntent.FirstNameChanged) = viewModelScope.launch {
        updateState {
            it.copy(firstName = EditableField(intent.value))
        }
    }

    private fun handleLastNameChanged(intent: ProfileIntent.LastNameChanged) = viewModelScope.launch {
        updateState {
            it.copy(lastName = EditableField(intent.value))
        }
    }

    private fun handlePhoneNumberChanged(intent: ProfileIntent.PhoneNumberChanged) = viewModelScope.launch {
        updateState {
            it.copy(phoneNumber = EditableField(intent.value))
        }
    }

    private fun handleEditProfile() = viewModelScope.launch {
        updateState {
            it.copy(isEditing = true)
        }
    }

    private fun handleSaveProfile() = viewModelScope.launch {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        
        val result = updateProfileUseCase.execute(
            firstName = state.value.firstName.value,
            lastName = state.value.lastName.value,
        )
        
        result.fold(
            onSuccess = { user ->
                updateState {
                    it.copy(
                        user = user,
                        isLoading = false,
                        isEditing = false
                    )
                }
            },
            onFailure = { exception ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to update profile"
                    )
                }
            }
        )
    }

    private fun handleCancelEdit() = viewModelScope.launch {
        loadUserProfile()
        updateState {
            it.copy(isEditing = false, errorMessage = null)
        }
    }

    private fun handleUploadPhoto(intent: ProfileIntent.UploadPhoto) = viewModelScope.launch {
        updateState { it.copy(isPhotoUploading = true, errorMessage = null) }
        
        val result = uploadProfilePhotoUseCase.execute(intent.imageUri)
        
        result.fold(
            onSuccess = { user ->
                updateState {
                    it.copy(
                        user = user,
                        isPhotoUploading = false
                    )
                }
            },
            onFailure = { exception ->
                updateState {
                    it.copy(
                        isPhotoUploading = false,
                        errorMessage = exception.message ?: "Failed to upload photo"
                    )
                }
            }
        )
    }

    private fun handleLogout() = viewModelScope.launch {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        
        try {
            logoutUseCase.execute()
            navigationProvider.navController.navigate(Routes.Login) {
                popUpTo(0) { inclusive = true }
            }
        } catch (e: Exception) {
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to logout"
                )
            }
        }
    }
} 