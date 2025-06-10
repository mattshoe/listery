package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerPlaceholder(
    modifier: Modifier = Modifier,
    width: Int? = null,
    height: Int? = null
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val targetValue = 1000f
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 1f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 1f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnim.value + (targetValue / 2), y = 0f),
        end = Offset(x = translateAnim.value - (targetValue / 2), y = 0f)
    )

    Box(
        modifier = modifier
            .clip(RectangleShape)
            .background(brush)
            .then(
                if (width != null && height != null) {
                    Modifier.size(width.dp, height.dp)
                } else if (width != null) {
                    Modifier.width(width.dp)
                } else if (height != null) {
                    Modifier.height(height.dp)
                } else {
                    Modifier
                }
            )
    )
}