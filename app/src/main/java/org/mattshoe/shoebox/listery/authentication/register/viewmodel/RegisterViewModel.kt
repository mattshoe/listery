package org.mattshoe.shoebox.listery.authentication.register.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import org.mattshoe.shoebox.listery.authentication.register.usecase.RegisterUseCase
import org.mattshoe.shoebox.listery.authentication.util.isValidEmail
import org.mattshoe.shoebox.listery.authentication.util.isValidPassword
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Routes
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val navigationProvider: NavigationProvider
) : ListeryViewModel<State, UserIntent>(State()) {

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.EmailUpdated -> handleEmailUpdated(intent)
            is UserIntent.PasswordUpdated -> handlePasswordUpdated(intent)
            is UserIntent.NameUpdated -> handleNameUpdated(intent)
            is UserIntent.PhoneNumberUpdated -> handlePhoneNumberUpdated(intent)
            is UserIntent.Register -> handleRegister(intent)
        }
    }

    private fun handleEmailUpdated(intent: UserIntent.EmailUpdated) = viewModelScope.launch {
        updateState {
            it.copy(
                email = EditableField(intent.value),
                allowSubmit = intent.value.isValidEmail() && it.password.value.isValidPassword()
            )
        }
    }

    private fun handlePasswordUpdated(intent: UserIntent.PasswordUpdated) = viewModelScope.launch {
        val isValidPassword = intent.value.isValidPassword()
        val error = if (isValidPassword) null else "You password must be at least 8 characters and contain at least 1 non-alphabetic character."
        updateState {
            it.copy(
                password = EditableField(
                    value = intent.value,
                    error = error
                ),
                allowSubmit = it.email.value.isValidEmail() && isValidPassword
            )
        }
    }

    private fun handleNameUpdated(intent: UserIntent.NameUpdated) = viewModelScope.launch {
        updateState {
            it.copy(name = EditableField(intent.value))
        }
    }

    private fun handlePhoneNumberUpdated(intent: UserIntent.PhoneNumberUpdated) = viewModelScope.launch {
        updateState {
            it.copy(phoneNumber = EditableField(intent.value))
        }
    }

    private fun handleRegister(intent: UserIntent.Register) = viewModelScope.launch {
        updateState { it.copy(loading = true) }
        val result = registerUseCase.execute(
            intent.state.email.value,
            intent.state.password.value
        )
        when (result) {
            is LoginResult.Success -> navigationProvider.navController.navigate(Routes.CookBook)
            is LoginResult.Error -> {
                updateState {
                    it.copy(
                        loading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}

