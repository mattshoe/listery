package org.mattshoe.shoebox.listery.login.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mattshoe.shoebox.listery.model.EditableField

// LoginViewModel: New login viewmodel following app's MVI pattern, stubbing out authentication logic for now
@HiltViewModel
class LoginViewModel @Inject constructor() : ListeryViewModel<LoginState, LoginIntent>(LoginState()) {
    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> updateState {
                it.copy(email = EditableField(intent.value), allowSignIn = intent.value.isNotBlank() && it.password.value.isNotBlank())
            }
            is LoginIntent.PasswordChanged -> updateState {
                it.copy(password = EditableField(intent.value), allowSignIn = it.email.value.isNotBlank() && intent.value.isNotBlank())
            }
            is LoginIntent.SignIn -> { /* TODO: Implement sign in */ }
            is LoginIntent.GoogleSignIn -> { /* TODO: Implement Google sign in */ }
            is LoginIntent.FacebookSignIn -> { /* TODO: Implement Facebook sign in */ }
        }
    }
}

data class LoginState(
    val email: EditableField<String> = EditableField(""),
    val password: EditableField<String> = EditableField(""),
    val allowSignIn: Boolean = false
)

sealed interface LoginIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object SignIn : LoginIntent
    data object GoogleSignIn : LoginIntent
    data object FacebookSignIn : LoginIntent
} 