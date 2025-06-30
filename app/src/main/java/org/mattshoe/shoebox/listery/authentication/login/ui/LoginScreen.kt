package org.mattshoe.shoebox.listery.authentication.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.authentication.common.ui.CredentialsInput
import org.mattshoe.shoebox.listery.authentication.common.ui.EmailInput
import org.mattshoe.shoebox.listery.authentication.common.ui.PasswordInput
import org.mattshoe.shoebox.listery.authentication.login.viewmodel.LoginIntent
import org.mattshoe.shoebox.listery.authentication.login.viewmodel.LoginViewModel
import org.mattshoe.shoebox.listery.cookbook.ui.gesturesDisabled
import org.mattshoe.shoebox.listery.ui.common.ClickableLinkText
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput
import org.mattshoe.shoebox.listery.ui.common.SubduedText
import org.mattshoe.shoebox.listery.ui.common.TextDivider
import org.mattshoe.shoebox.listery.ui.theme.onPrimaryDark

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue(state.password.value)) }

    if (state.password.value != passwordTextFieldValue.text)
        passwordTextFieldValue = passwordTextFieldValue.copy(text = state.password.value)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .gesturesDisabled(state.isLoading),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Listery!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            state.errorMessage?.let { errorMessage ->
                ErrorText(
                    text = errorMessage,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            CredentialsInput(
                email = state.email,
                password = state.password,
                onEmailUpdated = {
                    viewModel.handleIntent(LoginIntent.EmailChanged(it))
                },
                onPasswordUpdated = {
                    viewModel.handleIntent(LoginIntent.PasswordChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubduedText(
                    text = "Forgot password?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                ClickableLinkText(
                    text = "Reset your password",
                    style = MaterialTheme.typography.bodyMedium
                ) {
                    viewModel.handleIntent(
                        LoginIntent.ResetPassword
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            ListeryPrimaryButton(
                text = "Sign in",
                enabled = state.allowSignIn,
                onClick = {
                    viewModel.handleIntent(
                        LoginIntent.SignIn(
                            state.email.value,
                            state.password.value
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            TextDivider(
                "or",
                textStyle = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))
            ListeryPrimaryButton(
                text = "Continue with Google",
                onClick = { viewModel.handleIntent(LoginIntent.GoogleSignIn) },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
//            Spacer(modifier = Modifier.height(8.dp))
//            ListeryPrimaryButton(
//                text = "Continue with Facebook",
//                onClick = { viewModel.handleIntent(LoginIntent.FacebookSignIn) },
//                leadingIcon = {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_facebook),
//                        contentDescription = "Facebook Icon",
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubduedText(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                ClickableLinkText(
                    text = "Sign up",
                    style = MaterialTheme.typography.bodyMedium
                ) {
                    viewModel.handleIntent(
                        LoginIntent.Register
                    )
                }
            }

        }
    }
}

@Composable
fun ErrorText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall,
) {
    Text(
        text = text,
        style = style,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}