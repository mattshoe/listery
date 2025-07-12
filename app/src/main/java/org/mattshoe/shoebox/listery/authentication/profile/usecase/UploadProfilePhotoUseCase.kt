package org.mattshoe.shoebox.listery.authentication.profile.usecase

import android.content.Context
import android.net.Uri
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.memory.MemoryCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.authentication.model.User
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.loge
import javax.inject.Inject

class UploadProfilePhotoUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    @ApplicationContext private val applicationContext: Context
) {
    @OptIn(ExperimentalCoilApi::class)
    suspend fun execute(imageUri: Uri): Result<User> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                Result.failure(Exception("No user logged in"))
            } else {
                val originalPhotoUrl = currentUser.photoUrl.toString()
                val storageRef = firebaseStorage.reference
                    .child("users")
                    .child(currentUser.uid)
                    .child("profile_photo.jpg")
                storageRef.putFile(imageUri).await()
                val downloadUrl = storageRef.downloadUrl.await()
                val downLoadUrlString = downloadUrl.toString()

                ImageLoader(applicationContext).apply {
                    diskCache?.remove(originalPhotoUrl)
                    diskCache?.remove(downLoadUrlString)
                    memoryCache?.apply {
                        val keysToRemove = mutableListOf<MemoryCache.Key>()
                        keys.forEach { key ->
                            if (key.key.contains(originalPhotoUrl) || key.key.contains(downLoadUrlString)) {
                                keysToRemove.add(key)
                            }
                        }
                        keysToRemove.forEach {
                            remove(it)
                        }
                    }
                    memoryCache?.remove(MemoryCache.Key(originalPhotoUrl))
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUrl)
                    .build()
                currentUser.updateProfile(profileUpdates).await()

                Result.success(currentUser.toUser())
            }
        } catch (e: Exception) {
            loge("Failed to upload profile photo", e)
            Result.failure(e)
        }
    }
} 