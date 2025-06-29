package org.mattshoe.shoebox.listery.authentication.login.usecase

import org.mattshoe.shoebox.listery.authentication.login.usecase.EmailSignInUseCase
import org.mattshoe.shoebox.listery.authentication.login.usecase.GoogleSignInUseCase
import org.mattshoe.shoebox.listery.authentication.RegistrationSignInUseCase
import org.mattshoe.shoebox.listery.authentication.model.AuthRequest
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val emailSignInUseCase: EmailSignInUseCase,
    private val registrationSignInUseCase: RegistrationSignInUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) {
    suspend fun execute(request: AuthRequest): LoginResult {
        return when (request) {
            is AuthRequest.Email -> emailSignInUseCase.execute(request.email, request.password)
            is AuthRequest.Register -> registrationSignInUseCase.execute(request.email, request.password)
            AuthRequest.GoogleSignIn -> googleSignInUseCase.execute()
            AuthRequest.FacebookSignIn -> LoginResult.Error("NOT IMPLEMENTED")
        }
    }

}