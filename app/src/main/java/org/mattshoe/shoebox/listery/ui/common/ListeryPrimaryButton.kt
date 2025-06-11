package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.ui.theme.ListeryTheme

@Composable
fun ListeryPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .then(modifier),
        shape = RoundedCornerShape(24.dp)
    ) {
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