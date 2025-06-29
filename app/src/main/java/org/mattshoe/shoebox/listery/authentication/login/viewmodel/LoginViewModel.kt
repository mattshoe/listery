package org.mattshoe.shoebox.listery.authentication.login.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.authentication.model.LoginRequest
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import org.mattshoe.shoebox.listery.authentication.login.usecase.LoginUseCase
import org.mattshoe.shoebox.listery.authentication.util.isValidEmail
import org.mattshoe.shoebox.listery.authentication.util.isValidPassword
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
        updateState {
            it.copy(
                password = EditableField(value = intent.value),
                allowSignIn = it.email.value.isValidEmail() && intent.value.isValidPassword()
            )
        }
    }

    private fun handleSignIn(intent: LoginIntent.SignIn) = viewModelScope.launch {
        updateState {
            it.copy(
                password = EditableField("")
            )
        }
        login(LoginRequest.Email(intent.email, intent.password))
    }

    private fun handleResetPassword(intent: LoginIntent.ResetPassword) = viewModelScope.launch {
        navigationProvider.navController.navigate(Routes.ResetPassword)
    }

    private fun handleGoogleSignIn(intent: LoginIntent.GoogleSignIn) = viewModelScope.launch {
        login(LoginRequest.GoogleSignIn)
    }

    private fun handleFacebookSignIn(intent: LoginIntent.FacebookSignIn) = viewModelScope.launch {
        login(LoginRequest.FacebookSignIn)
    }

    private fun handleRegister(intent: LoginIntent.Register) = viewModelScope.launch {
        navigationProvider.navController.navigate(Routes.Register) {
            popUpTo(0) { inclusive = true }
        }
    }

    private suspend fun login(request: LoginRequest) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        val result = loginUseCase.execute(request)
        when (result) {
            is LoginResult.Success -> {
                navigationProvider.navController.navigate(Routes.CookBook) {
                    popUpTo(0) { inclusive = true }
                }
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

}

