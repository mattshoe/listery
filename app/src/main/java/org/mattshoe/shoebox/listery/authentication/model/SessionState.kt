package org.mattshoe.shoebox.listery.authentication.model

sealed interface SessionState {
    data object Anonymous: SessionState
    data class LoggedIn(
        val user: User,
        val sessionId: String,
        val refreshId: String
    ): SessionState
}

