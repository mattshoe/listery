package org.mattshoe.shoebox.listery.authentication.resetpassword.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.authentication.resetpassword.usecase.ResetPasswordUseCase
import org.mattshoe.shoebox.listery.authentication.util.isValidEmail
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import javax.inject.Inject

data class State(
    val loading: Boolean = false,
    val emailSent: Boolean = false,
    val allowSubmit: Boolean = false,
    val email: EditableField<String> = EditableField(""),
)

sealed interface UserIntent {
    data class EmailUpdated(val value: String): UserIntent
    data class SendPasswordResetEmail(val email: String): UserIntent
    data object AcknowledgeSentEmail: UserIntent
}

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val navigationProvider: NavigationProvider
) : ListeryViewModel<State, UserIntent>(State()) {

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.EmailUpdated -> handleEmailUpdated(intent)
            is UserIntent.SendPasswordResetEmail -> handleSendEmail(intent)
            is UserIntent.AcknowledgeSentEmail -> handleAcknowledgeSentEmail(intent)
        }
    }

    private fun handleEmailUpdated(intent: UserIntent.EmailUpdated) = viewModelScope.launch {
        updateState {
            it.copy(
                email = EditableField(intent.value),
                allowSubmit = intent.value.isValidEmail()
            )
        }
    }

    private fun handleSendEmail(intent: UserIntent.SendPasswordResetEmail) = viewModelScope.launch {
        resetPasswordUseCase.execute(intent.email)
        updateState {
            it.copy(emailSent = true, loading = false)
        }
    }

    private fun handleAcknowledgeSentEmail(intent: UserIntent.AcknowledgeSentEmail) = viewModelScope.launch {
        navigationProvider.navController.popBackStack()
    }
}

