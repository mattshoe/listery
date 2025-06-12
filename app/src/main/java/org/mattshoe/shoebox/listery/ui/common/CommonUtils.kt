package org.mattshoe.shoebox.listery.ui.common

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri

@Composable
fun ClickableLinkText(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Text(
        text = url,
        color = Color.Blue,
        modifier = modifier
            .clickable {
                try {
                    var uri = url
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        uri = "http://$url"
                    }
                    val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
                    context.startActivity(intent)
                } catch (e: Throwable) {
                    Toast.makeText(context, "Unable to open this link", Toast.LENGTH_SHORT).show()
                }

            },
        style = MaterialTheme.typography.bodySmall.copy(
            textDecoration = TextDecoration.Underline
        )
    )
}
