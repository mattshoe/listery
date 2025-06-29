# Firebase Authentication Usage Guide

## Overview
This project now has a complete Firebase Authentication system implemented with the following features:

- Email/Password Sign In
- Email/Password Registration  
- Google Sign In
- User Session Management
- Error Handling

## How to Use

### 1. Sign In with Email/Password
```kotlin
// In your ViewModel or UseCase
val result = loginUseCase.execute(LoginRequest.Email("user@example.com", "password123"))

when (result) {
    is LoginResult.Success -> {
        val user = result.user
        // Navigate to main screen or update UI
    }
    is LoginResult.Error -> {
        val errorMessage = result.message
        // Show error to user
    }
}
```

### 2. Register New User
```kotlin
val result = loginUseCase.execute(LoginRequest.Register("newuser@example.com", "password123"))

when (result) {
    is LoginResult.Success -> {
        val user = result.user
        // User created successfully
    }
    is LoginResult.Error -> {
        val errorMessage = result.message
        // Show error to user
    }
}
```

### 3. Google Sign In
```kotlin
val result = loginUseCase.execute(LoginRequest.Google)

when (result) {
    is LoginResult.Success -> {
        val user = result.user
        // User signed in with Google
    }
    is LoginResult.Error -> {
        val errorMessage = result.message
        // Show error to user
    }
}
```

### 4. Check Current User
```kotlin
val currentUser = loginUseCase.getCurrentUser()
if (currentUser != null) {
    // User is signed in
} else {
    // User is not signed in
}
```

### 5. Sign Out
```kotlin
loginUseCase.signOut()
// User is now signed out
```

## UI Integration

The `LoginViewModel` already handles all the authentication logic and provides:

- `isLoading`: Boolean to show loading state
- `errorMessage`: String? to display error messages
- `allowSignIn`: Boolean to enable/disable sign in button

### Example UI State Handling
```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    
    if (state.isLoading) {
        CircularProgressIndicator()
    }
    
    state.errorMessage?.let { error ->
        Text(text = error, color = Color.Red)
    }
    
    Button(
        onClick = { 
            viewModel.handleIntent(LoginIntent.SignIn(email, password))
        },
        enabled = state.allowSignIn && !state.isLoading
    ) {
        Text("Sign In")
    }
}
```

## Setup Requirements

1. **Firebase Project**: Make sure you have a Firebase project set up
2. **google-services.json**: Place this file in the `app/` directory
3. **Dependencies**: Firebase Auth is already included in your build.gradle.kts
4. **Google Services Plugin**: Already applied in your build.gradle.kts

## Error Handling

The system handles common Firebase Auth errors:
- Invalid email/password
- User not found
- Weak password
- Email already in use
- Network errors
- Google sign-in failures

All errors are returned as `LoginResult.Error` with descriptive messages.

## Security Notes

- Passwords are handled securely by Firebase
- Google Sign In uses the official Google Identity library
- No sensitive data is stored locally
- All authentication is handled server-side by Firebase

## Next Steps

To complete the integration, you'll want to:

1. Add navigation logic in the success cases
2. Implement a session management system
3. Add password reset functionality
4. Add email verification
5. Implement Facebook authentication (currently mapped to Google) 