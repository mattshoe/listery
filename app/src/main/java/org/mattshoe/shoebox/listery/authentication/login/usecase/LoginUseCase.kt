package org.mattshoe.shoebox.listery.authentication.login.usecase

import org.mattshoe.shoebox.listery.authentication.model.LoginRequest
import org.mattshoe.shoebox.listery.authentication.model.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val emailSignInUseCase: EmailSignInUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val facebookSignInUseCase: FacebookSignInUseCase
) {
    suspend fun execute(request: LoginRequest): LoginResult {
        return when (request) {
            is LoginRequest.Email -> emailSignInUseCase.execute(request.email, request.password)
            is LoginRequest.GoogleSignIn -> googleSignInUseCase.execute()
            is LoginRequest.FacebookSignIn -> facebookSignInUseCase.execute()
        }
    }

}