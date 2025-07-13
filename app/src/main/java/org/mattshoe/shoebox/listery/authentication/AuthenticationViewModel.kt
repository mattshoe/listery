package org.mattshoe.shoebox.listery.authentication

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.authentication.data.SessionRepository
import org.mattshoe.shoebox.listery.authentication.model.SessionState
import org.mattshoe.shoebox.listery.authentication.profile.usecase.LogoutUseCase
import org.mattshoe.shoebox.listery.common.ListeryViewModel
import org.mattshoe.shoebox.listery.logging.logd
import javax.inject.Inject

sealed interface AuthIntent {
    data object Logout: AuthIntent
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val logoutUseCase: LogoutUseCase
): ListeryViewModel<SessionState, AuthIntent>(
    sessionRepository.state.value
) {

    init {
        sessionRepository.state
            .onEach { session ->
                logd("Session Updated -> ProfilePic - ${(session as? SessionState.LoggedIn)?.user?.photoUrl}")
                updateState {
                    session
                }
            }.launchIn(viewModelScope)
    }

    override fun handleIntent(intent: AuthIntent) {
        viewModelScope.launch {
            when (intent) {
                is AuthIntent.Logout -> logoutUseCase.execute()
            }
        }
    }
}