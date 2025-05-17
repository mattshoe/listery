package org.mattshoe.shoebox.listery.ui.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun ClickableLinkText(
    uri: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Text(
        text = uri,
        color = Color.Blue,
        modifier = modifier
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context.startActivity(intent)
            },
        style = MaterialTheme.typography.bodySmall.copy(
            textDecoration = TextDecoration.Underline
        )
    )
}
