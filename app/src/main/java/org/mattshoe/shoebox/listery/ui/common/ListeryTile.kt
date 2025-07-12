package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ListeryTile(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .then(modifier),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        content()
    }
}