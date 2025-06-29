package org.mattshoe.shoebox.listery.authentication.resetpassword.ui

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
import org.mattshoe.shoebox.listery.authentication.common.ui.EmailInput
import org.mattshoe.shoebox.listery.authentication.login.ui.ErrorText
import org.mattshoe.shoebox.listery.authentication.login.viewmodel.LoginIntent
import org.mattshoe.shoebox.listery.authentication.login.viewmodel.LoginViewModel
import org.mattshoe.shoebox.listery.authentication.resetpassword.viewmodel.ResetPasswordViewModel
import org.mattshoe.shoebox.listery.authentication.resetpassword.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.cookbook.ui.gesturesDisabled
import org.mattshoe.shoebox.listery.ui.common.ClickableLinkText
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput
import org.mattshoe.shoebox.listery.ui.common.SubduedText
import org.mattshoe.shoebox.listery.ui.common.TextDivider

@Composable
fun ResetPasswordBottomSheet(
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var emailTextFieldValue by remember { mutableStateOf(TextFieldValue(state.email.value)) }

    if (state.email.value != emailTextFieldValue.text)
        emailTextFieldValue = emailTextFieldValue.copy(text = state.email.value)

    ListeryBottomSheet {
        Column(
            modifier = Modifier
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.emailSent) {
                Text(
                    text = "Success!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                SubduedText(
                    text = "If we have an account with that email, you will receive an email soon. Once you've updated your password there, you can come back to sign in with your new password.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                ListeryPrimaryButton(
                    text = "Got it!",
                    onClick = {
                        viewModel.handleIntent(UserIntent.AcknowledgeSentEmail)
                    }
                )
            } else {
                Text(
                    text = "Reset your password",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(32.dp))
                EmailInput(state.email) {
                    viewModel.handleIntent(UserIntent.EmailUpdated(it))
                }
                Spacer(modifier = Modifier.height(20.dp))
                ListeryPrimaryButton(
                    text = "Send email to reset password",
                    enabled = state.allowSubmit,
                    onClick = {
                        viewModel.handleIntent(UserIntent.SendPasswordResetEmail(emailTextFieldValue.text))
                    }
                )
            }
        }
    }
}

