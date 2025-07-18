package org.mattshoe.shoebox.listery.authentication.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.mattshoe.shoebox.listery.authentication.model.SessionState
import org.mattshoe.shoebox.listery.authentication.model.User
import org.mattshoe.shoebox.listery.authentication.util.toSessionState
import org.mattshoe.shoebox.listery.authentication.util.toUser
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class SessionRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val _session = MutableStateFlow<SessionState>(
        firebaseAuth.currentUser.toSessionState()
    )

    val state: StateFlow<SessionState> = _session

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
                val user = firebaseUser.toUser()
                _session.update {
                    session.copy(
                        user = user
                    )
                }
            }
        }
    }
}