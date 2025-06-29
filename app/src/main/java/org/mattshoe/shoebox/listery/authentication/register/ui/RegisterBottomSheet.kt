package org.mattshoe.shoebox.listery.authentication.register.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.authentication.common.ui.CredentialsInput
import org.mattshoe.shoebox.listery.authentication.login.viewmodel.LoginIntent
import org.mattshoe.shoebox.listery.authentication.register.viewmodel.RegisterViewModel
import org.mattshoe.shoebox.listery.authentication.register.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton

@Composable
fun RegisterBottomSheet(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ListeryBottomSheet {
        Column(
            modifier = Modifier
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Your Account!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            CredentialsInput(
                email = state.email,
                password = state.password,
                onEmailUpdated = {
                    viewModel.handleIntent(UserIntent.EmailUpdated(it))
                },
                onPasswordUpdated = {
                    viewModel.handleIntent(UserIntent.PasswordUpdated(it))
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            ListeryPrimaryButton(
                text = "Get cookin'!",
                enabled = state.allowSubmit,
                onClick = {
                    viewModel.handleIntent(
                        UserIntent.Register(state)
                    )
                }
            )
        }
    }
}

