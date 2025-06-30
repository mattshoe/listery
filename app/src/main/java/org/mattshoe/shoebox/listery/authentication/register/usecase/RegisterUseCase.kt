package org.mattshoe.shoebox.listery.authentication.register.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun execute(
        email: String,
        password: String
    ): LoginResult {
        return try {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
                .user?.let {
                    LoginResult.Success(it.toUser())
                }
                ?: run {
                    firebaseAuth.signOut()
                    LoginResult.Error("Registration failed, please try again.")
                }
        } catch (e: Exception) {
            loge("Email registration failed", e)
            LoginResult.Error("Registration failed, please try again.")
        }
    }
}