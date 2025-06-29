package org.mattshoe.shoebox.listery.authentication

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mattshoe.shoebox.listery.authentication.model.SessionState
import org.mattshoe.shoebox.listery.authentication.util.toSessionState
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val _session = MutableStateFlow<SessionState>(
        firebaseAuth.currentUser.toSessionState()
    )
    val session: StateFlow<SessionState> = _session.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener { state ->
            _session.update {
                state.currentUser.toSessionState()
            }
        }
    }
}
