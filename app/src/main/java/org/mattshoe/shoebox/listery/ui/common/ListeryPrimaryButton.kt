package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.ui.theme.ListeryTheme

@Composable
fun ListeryPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable (() -> Unit))? = null,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .then(modifier),
        shape = RoundedCornerShape(24.dp)
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(name = "Enabled Button", showBackground = true)
@Composable
private fun PreviewListeryPrimaryButtonEnabled() {
    ListeryTheme {
        ListeryPrimaryButton(
            text = "Click Me",
            onClick = {}
        )
    }
}

@Preview(name = "Disabled Button", showBackground = true)
@Composable
private fun PreviewListeryPrimaryButtonDisabled() {
    ListeryTheme {
        ListeryPrimaryButton(
            text = "Disabled",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(name = "Button with Icon", showBackground = true)
@Composable
private fun PreviewListeryPrimaryButtonWithIcon() {
    ListeryTheme {
        ListeryPrimaryButton(
            text = "Sign in with Google",
            onClick = {},
            leadingIcon = {
                Box(
                    Modifier
                        .size(20.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                )
            }
        )
    }
}