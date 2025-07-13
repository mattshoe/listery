package org.mattshoe.shoebox.listery.authentication.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mattshoe.shoebox.listery.authentication.model.SessionState
import org.mattshoe.shoebox.listery.authentication.model.User
import org.mattshoe.shoebox.listery.authentication.util.toSessionState
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.logd
import java.util.UUID
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val _session = MutableStateFlow<SessionState>(
        firebaseAuth.currentUser.toSessionState()
    )
    val state: StateFlow<SessionState> = _session.asStateFlow()

    val currentUser: User?
        get() = when (val session = _session.value) {
            is SessionState.LoggedIn -> session.user
            is SessionState.Anonymous -> null
        }

    init {
        firebaseAuth.addAuthStateListener { state ->
            _session.update {
                state.currentUser.toSessionState()
            }
        }
    }

    fun refreshProfile() {
        (_session.value as? SessionState.LoggedIn)?.let { session ->
            firebaseAuth.currentUser?.let { firebaseUser ->
                val user = firebaseUser.toUser().also {
                    logd("Refresh Session -> ProfilePic - ${it.photoUrl} ")
                }
                _session.update {
                    val newSession = session.copy(
                        user = user,
                        refreshId = UUID.randomUUID().toString()
                    )

                    logd("oldSession == newSession -> ${it == newSession}")

                    newSession
                }
            }
        }
    }
}