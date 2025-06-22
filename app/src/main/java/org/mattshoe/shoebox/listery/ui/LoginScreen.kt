package org.mattshoe.shoebox.listery.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var emailTextFieldValue by remember { mutableStateOf(TextFieldValue(state.email.value)) }
    var passwordTextFieldValue by remember { mutableStateOf(TextFieldValue(state.password.value)) }

    if (state.email.value != emailTextFieldValue.text)
        emailTextFieldValue = emailTextFieldValue.copy(text = state.email.value)

    if (state.password.value != passwordTextFieldValue.text)
        passwordTextFieldValue = passwordTextFieldValue.copy(text = state.password.value)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
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
            ListeryTextInput(
                value = emailTextFieldValue,
                placeholder = "Email",
                onValueChange = {
                    emailTextFieldValue = it
                    viewModel.handleIntent(LoginIntent.EmailChanged(it.text))
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_account),
                        contentDescription = "Email Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ListeryTextInput(
                value = passwordTextFieldValue,
                placeholder = "Password",
                password = true,
                onValueChange = {
                    passwordTextFieldValue = it
                    viewModel.handleIntent(LoginIntent.PasswordChanged(it.text))
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = "Password Icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                maxLines = 1,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default.copy(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            ListeryPrimaryButton(
                text = "Sign in",
                enabled = state.allowSignIn,
                onClick = { viewModel.handleIntent(LoginIntent.SignIn) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ListeryPrimaryButton(
                text = "Sign in with Google",
                onClick = { viewModel.handleIntent(LoginIntent.GoogleSignIn) },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ListeryPrimaryButton(
                text = "Sign in with Facebook",
                onClick = { viewModel.handleIntent(LoginIntent.FacebookSignIn) },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Facebook Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
    }
} 