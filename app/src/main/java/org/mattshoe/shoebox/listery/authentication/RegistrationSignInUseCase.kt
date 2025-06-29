package org.mattshoe.shoebox.listery.authentication

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import org.mattshoe.shoebox.listery.authentication.model.User
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject

class RegistrationSignInUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun execute(email: String, password: String): LoginResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                LoginResult.Success(firebaseUser.toUser())
            } ?: LoginResult.Error("Registration failed")
        } catch (e: Exception) {
            loge("Email registration failed", e)
            LoginResult.Error(e.message ?: "Registration failed")
        }
    }
}