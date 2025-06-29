package org.mattshoe.shoebox.listery.authentication

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import org.mattshoe.shoebox.listery.authentication.model.User
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.logd
import javax.inject.Inject

class EmailSignInUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun execute(email: String, password: String): LoginResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.credential
            result.user?.let { firebaseUser ->
                LoginResult.Success(firebaseUser.toUser())
            } ?: LoginResult.Error("Authentication failed")
        } catch (e: Throwable) {
            logd("Email sign in failed: $e")
            LoginResult.Error("Invalid email or password. Please check and try again.")
        }
    }
}