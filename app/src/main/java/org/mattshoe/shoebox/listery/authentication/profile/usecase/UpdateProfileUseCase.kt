package org.mattshoe.shoebox.listery.authentication.profile.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.authentication.data.SessionRepository
import org.mattshoe.shoebox.listery.authentication.model.User
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sessionRepository: SessionRepository
) {
    suspend fun execute(
        firstName: String?,
        lastName: String?,
    ): Result<User> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                Result.failure(Exception("No user logged in"))
            } else {
                val fullName = buildString {
                    if (!firstName.isNullOrBlank()) append(firstName)
                    if (!lastName.isNullOrBlank()) {
                        if (isNotEmpty()) append(" ")
                        append(lastName)
                    }
                }
                
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(if (fullName.isNotBlank()) fullName else null)
                    .build()
                
                currentUser.updateProfile(profileUpdates).await()

                sessionRepository.refreshProfile()
                
                // Note: Phone number updates require additional verification
                // For now, we'll just update the display name
                
                Result.success(currentUser.toUser())
            }
        } catch (e: Exception) {
            loge("Failed to update profile", e)
            Result.failure(e)
        }
    }
} 