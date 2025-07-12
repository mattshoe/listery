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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri

@Composable
fun ClickableLinkText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    onclick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Text(
        text = text,
        color = Color.Blue,
        modifier = modifier
            .clickable {
                try {
                    onclick?.invoke() ?: run {
                        var uri = text
                        if (!text.startsWith("http://") && !text.startsWith("https://")) {
                            uri = "http://$text"
                        }
                        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
                        context.startActivity(intent)
                    }
                } catch (e: Throwable) {
                    Toast.makeText(context, "Unable to open this link", Toast.LENGTH_SHORT).show()
                }

            },
        style = style.copy(
            textDecoration = TextDecoration.Underline
        )
    )
}
