package org.mattshoe.shoebox.listery.authentication.login.usecase

import com.google.firebase.auth.FirebaseAuth
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import javax.inject.Inject

class FacebookSignInUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun execute(): LoginResult {
        return LoginResult.Error("NOT IMPLEMENTED")
    }

}