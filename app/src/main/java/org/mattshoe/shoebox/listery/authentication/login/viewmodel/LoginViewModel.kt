package org.mattshoe.shoebox.listery.authentication.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.authentication.model.AuthRequest
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import org.mattshoe.shoebox.listery.authentication.login.usecase.LoginUseCase
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.navigation.NavigationProvider
import org.mattshoe.shoebox.listery.navigation.Routes
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val navigationProvider: NavigationProvider
) : ListeryViewModel<LoginState, LoginIntent>(LoginState()) {

    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> handleEmailChanged(intent)
            is LoginIntent.PasswordChanged -> handlePasswordChanged(intent)
            is LoginIntent.SignIn -> handleSignIn(intent)
            is LoginIntent.ResetPassword -> handleResetPassword(intent)
            is LoginIntent.Register -> handleRegister(intent)
            is LoginIntent.GoogleSignIn -> handleGoogleSignIn(intent)
            is LoginIntent.FacebookSignIn -> handleFacebookSignIn(intent)
        }
    }

    private fun handleEmailChanged(intent: LoginIntent.EmailChanged) = viewModelScope.launch {
        updateState {
            it.copy(
                email = EditableField(intent.value),
                allowSignIn = intent.value.isValidEmail() && it.password.value.isValidPassword()
            )
        }
    }

    private fun handlePasswordChanged(intent: LoginIntent.PasswordChanged) = viewModelScope.launch {
        val isValidPassword = intent.value.isValidPassword()
        val error = if (isValidPassword) null else "You password must be at least 8 characters and contain at least 1 non-alphabetic character."
        updateState {
            it.copy(
                password = EditableField(value = intent.value, error = error),
                allowSignIn = it.email.value.isValidEmail() && isValidPassword
            )
        }
    }

    private fun handleSignIn(intent: LoginIntent.SignIn) = viewModelScope.launch {
        updateState {
            it.copy(
                password = EditableField("")
            )
        }
        login(AuthRequest.Email(intent.email, intent.password))
    }

    private fun handleResetPassword(intent: LoginIntent.ResetPassword) = viewModelScope.launch {

    }

    private fun handleGoogleSignIn(intent: LoginIntent.GoogleSignIn) = viewModelScope.launch {
        login(AuthRequest.GoogleSignIn)
    }

    private fun handleFacebookSignIn(intent: LoginIntent.FacebookSignIn) = viewModelScope.launch {
        login(AuthRequest.FacebookSignIn)
    }

    private fun handleRegister(intent: LoginIntent.Register) = viewModelScope.launch {

    }

    private suspend fun login(request: AuthRequest) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        val result = loginUseCase.execute(request)
        when (result) {
            is LoginResult.Success -> {
                navigationProvider.navController.navigate(Routes.CookBook)
            }
            is LoginResult.Error -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    private fun String.isValidPassword(): Boolean {
        return length >= 8 && any { !it.isLetter() }
    }

    private fun String.isValidEmail(): Boolean {
        return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}

