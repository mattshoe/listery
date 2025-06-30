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

class FacebookSignInUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun execute(): LoginResult {
        return LoginResult.Error("NOT IMPLEMENTED")
    }

}