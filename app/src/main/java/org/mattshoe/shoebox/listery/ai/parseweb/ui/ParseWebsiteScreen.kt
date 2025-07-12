package org.mattshoe.shoebox.listery.ai.parseweb.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mattshoe.shoebox.listery.ai.parseweb.viewmodel.ParseWebsiteViewModel
import org.mattshoe.shoebox.listery.ai.parseweb.viewmodel.UserIntent
import org.mattshoe.shoebox.listery.authentication.login.ui.ErrorText
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet
import org.mattshoe.shoebox.listery.ui.common.ListeryPrimaryButton
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun ParseWebsiteScreen(
    viewModel: ParseWebsiteViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ListeryBottomSheet {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ListeryTextInput(
                value = EditableField(
                    value = state.url,
                    enabled = !state.isLoading
                ),
                placeholder = "Enter website"
            ) {
                viewModel.handleIntent(UserIntent.UpdateUrl(it))
            }

            // Error Display
            state.error?.let { error ->
                ErrorText(
                    error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ListeryPrimaryButton(
                text = "Read website",
                enabled = state.allowSubmit,
                leadingIcon = {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            ) {
                viewModel.handleIntent(UserIntent.ParseWebsite(state.url))
            }
        }
    }
}
