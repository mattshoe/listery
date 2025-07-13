package org.mattshoe.shoebox.listery.authentication.profile.usecase

import android.content.Context
import android.net.Uri
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.authentication.data.SessionRepository
import org.mattshoe.shoebox.listery.authentication.model.User
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject
import androidx.core.net.toUri
import coil.memory.MemoryCache
import kotlinx.coroutines.supervisorScope
import java.util.UUID

class UploadProfilePhotoUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val sessionRepository: SessionRepository,
    @ApplicationContext private val applicationContext: Context
) {
    companion object {
        private val imageKeyRegex = Regex("profile_photo.*?\\.jpg")
    }

    @OptIn(ExperimentalCoilApi::class)
    suspend fun execute(imageUri: Uri): Result<User> = supervisorScope {
        try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                Result.failure(Exception("No user logged in"))
            } else {
                val originalPhotoUrl = currentUser.photoUrl.toString()
                val userStorage = firebaseStorage.reference
                    .child("users")
                    .child(currentUser.uid)

                val profilePic = userStorage.child("profile_photo-${UUID.randomUUID()}.jpg").apply {
                    putFile(imageUri).await()
                }
                val downloadUrl = profilePic.downloadUrl.await()

                ImageLoader(applicationContext).apply {
                    diskCache?.remove(originalPhotoUrl)
                    memoryCache?.remove(MemoryCache.Key(originalPhotoUrl))
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUrl)
                    .build()
                currentUser.updateProfile(profileUpdates).await()

                if (originalPhotoUrl.isNotBlank()) {
                    try {
                        val photoId = imageKeyRegex.find(originalPhotoUrl)?.value ?: ""
                        userStorage.child(photoId)
                            .delete()
                            .await()
                    } catch (e: Throwable) {
                        loge(e)
                    }
                }
                sessionRepository.refreshProfile()

                Result.success(currentUser.toUser())
            }
        } catch (e: Exception) {
            loge("Failed to upload profile photo", e)
            Result.failure(e)
        }
    }
} 