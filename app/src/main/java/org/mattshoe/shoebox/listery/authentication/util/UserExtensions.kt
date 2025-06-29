package org.mattshoe.shoebox.listery.authentication.util

import com.google.firebase.auth.FirebaseUser
import org.mattshoe.shoebox.listery.authentication.model.SessionState
import org.mattshoe.shoebox.listery.authentication.model.User

fun FirebaseUser?.toSessionState(): SessionState {
    return this?.let {
        SessionState.LoggedIn(
            it.toUser()
        )
    } ?: SessionState.Anonymous
}

fun FirebaseUser.toUser(): User {
    return User(
        id = this.uid,
        name = this.displayName,
        email = this.email,
        photoUrl = this.photoUrl?.toString(),
        isEmailVerified = this.isEmailVerified,
        phoneNumber = this.phoneNumber
    )
}