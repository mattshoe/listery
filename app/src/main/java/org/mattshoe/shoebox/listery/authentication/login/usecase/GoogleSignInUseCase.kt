package org.mattshoe.shoebox.listery.authentication.login.usecase

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import org.mattshoe.shoebox.listery.authentication.util.toUser
import org.mattshoe.shoebox.listery.logging.loge
import org.mattshoe.shoebox.listery.util.ActivityProvider
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val activityProvider: ActivityProvider,
    private val credentialManager: CredentialManager,
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun execute(): LoginResult {
        val googleIdOption: CredentialOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(applicationContext.getString(R.string.default_web_client_id))
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            activityProvider.currentActivity?.let { activity ->
                val result = credentialManager.getCredential(
                    request = request,
                    context = activity,
                )
                handleGoogleCredential(result)
            } ?: LoginResult.Error("No activity available")
        } catch (e: GetCredentialException) {
            loge("Google sign in failed", e)
            when (e) {
                is NoCredentialException -> {
                    LoginResult.Error("Google Sign-In not available. Please try email/password sign in.")
                }
                else -> {
                    LoginResult.Error("Google Sign-In failed: ${e.message ?: "Unknown error"}")
                }
            }
        } catch (e: Exception) {
            loge("Unexpected error during Google sign in", e)
            LoginResult.Error("Google Sign-In failed: ${e.message ?: "Unknown error"}")
        }
    }

    private suspend fun handleGoogleCredential(result: GetCredentialResponse): LoginResult {
        val credential = result.credential

        return when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.Companion.createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken

                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()

                        authResult.user?.let { firebaseUser ->
                            LoginResult.Success(firebaseUser.toUser())
                        } ?: LoginResult.Error("Google authentication failed")
                    } catch (e: GoogleIdTokenParsingException) {
                        loge("Received an invalid google id token response", e)
                        LoginResult.Error("Invalid Google token")
                    } catch (e: Exception) {
                        loge("Google authentication failed", e)
                        LoginResult.Error(e.message ?: "Google authentication failed")
                    }
                } else {
                    loge("Unexpected type of credential")
                    LoginResult.Error("Unexpected credential type")
                }
            }
            else -> {
                loge("Unexpected type of credential")
                LoginResult.Error("Unexpected credential type")
            }
        }
    }
}