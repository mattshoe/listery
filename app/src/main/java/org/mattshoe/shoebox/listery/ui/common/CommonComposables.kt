package org.mattshoe.shoebox.listery.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.navigation.LocalNavController
import org.mattshoe.shoebox.listery.ui.BottomNavItem

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Level1AppBar(title: String) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            scrolledContainerColor = Color.Green
        ),
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_account),
                    contentDescription = "User Profile",
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_more_options),
                    contentDescription = "More Options",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

data class TopBarIcon(
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Level2AppBar(
    actions: List<TopBarIcon>,
    onBack: () -> Unit = {}
) {
    val navController = LocalNavController.current
    TopAppBar(
        title = {},
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            scrolledContainerColor = Color.Green
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    onBack()
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            actions.forEach {
                IconButton(onClick = it.onClick) {
                    Icon(it.icon, contentDescription = it.contentDescription)
                }
            }
        }
    )
}

@Composable
fun TextDivider(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    dividerColor: Color = MaterialTheme.colorScheme.outline,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left divider line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(dividerColor)
        )
        
        // Centered text with padding
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        // Right divider line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(dividerColor)
        )
    }
}

@Composable
fun CustomTextDivider(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    dividerColor: Color = MaterialTheme.colorScheme.outline,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    horizontalPadding: Dp = 16.dp,
    dividerHeight: Dp = 1.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left divider line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(dividerHeight)
                .background(dividerColor)
        )
        
        // Centered text with padding
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )
        
        // Right divider line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(dividerHeight)
                .background(dividerColor)
        )
    }
}