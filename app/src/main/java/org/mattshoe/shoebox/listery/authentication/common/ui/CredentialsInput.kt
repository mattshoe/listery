package org.mattshoe.shoebox.listery.authentication.common.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.model.EditableField

@Composable
fun CredentialsInput(
    email: EditableField<String>,
    password: EditableField<String>,
    onEmailUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit
) {
    EmailInput(email) {
        onEmailUpdated(it)
    }
    Spacer(modifier = Modifier.height(16.dp))
    PasswordInput(password) {
        onPasswordUpdated(it)
    }
}