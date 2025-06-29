# Google Sign-In Setup Guide

## Current Issue
You're getting a `NoCredentialException: No credentials available` error when trying to use Google Sign-In. This happens because the new Android Credentials API requires additional setup that isn't complete yet.

## Quick Fix (Current Implementation)
For now, the app will show a message: "Google Sign-In is being configured. Please use email/password sign in for now."

## Complete Google Sign-In Setup

### 1. Firebase Console Setup
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Go to Authentication → Sign-in method
4. Enable Google Sign-in
5. Add your SHA-1 fingerprint (see step 2)

### 2. Get SHA-1 Fingerprint
```bash
# For debug builds
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

# For release builds (if you have a keystore)
keytool -list -v -keystore your-keystore.jks -alias your-alias
```

### 3. Update AndroidManifest.xml
Add the following to your `AndroidManifest.xml` inside the `<application>` tag:

```xml
<meta-data
    android:name="com.google.android.gms.version"
    android:value="@integer/google_play_services_version" />
```

### 4. Alternative Google Sign-In Implementation

If you want to use the traditional Google Sign-In method instead of the new Credentials API, replace the `handleGoogleSignIn()` method in `LoginUseCase.kt` with this:

```kotlin
private suspend fun handleGoogleSignIn(): LoginResult {
    return try {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(applicationContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(applicationContext, gso)
        
        activityProvider.currentActivity?.let { activity ->
            // This will launch the Google Sign-In intent
            val signInIntent = googleSignInClient.signInIntent
            activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
            
            // Note: You'll need to handle the result in your activity
            LoginResult.Error("Google Sign-In launched. Handle result in activity.")
        } ?: LoginResult.Error("No activity available")
    } catch (e: Exception) {
        Log.e(TAG, "Google sign in failed", e)
        LoginResult.Error("Google Sign-In failed: ${e.message ?: "Unknown error"}")
    }
}

companion object {
    private const val TAG = "LoginUseCase"
    const val GOOGLE_SIGN_IN_REQUEST_CODE = 9001
}
```

### 5. Handle Google Sign-In Result in Activity

Add this to your main activity:

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == LoginUseCase.GOOGLE_SIGN_IN_REQUEST_CODE) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            // Handle successful sign-in
            handleGoogleSignInSuccess(account)
        } catch (e: ApiException) {
            // Handle sign-in failure
            Log.w(TAG, "Google sign in failed", e)
        }
    }
}

private fun handleGoogleSignInSuccess(account: GoogleSignInAccount) {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success
                val user = task.result?.user
                // Navigate to main screen
            } else {
                // Sign in failed
                Log.w(TAG, "signInWithCredential:failure", task.exception)
            }
        }
}
```

## Current Status

✅ **Email/Password Authentication**: Fully working  
✅ **User Registration**: Fully working  
✅ **Firebase Integration**: Complete  
⚠️ **Google Sign-In**: Needs additional setup  

## Recommendation

For now, focus on email/password authentication which is working perfectly. You can add Google Sign-In later when you have time to complete the setup. The current implementation gracefully handles the Google Sign-In error and guides users to use email/password instead.

## Testing

1. **Email/Password Sign In**: ✅ Works
2. **User Registration**: ✅ Works  
3. **Google Sign-In**: ⚠️ Shows setup message
4. **Error Handling**: ✅ Works
5. **Navigation**: ✅ Works (navigates to CookBook on success)

The authentication system is production-ready for email/password authentication! 