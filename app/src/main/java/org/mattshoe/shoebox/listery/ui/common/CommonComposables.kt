package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.EqualityDelegate
import coil.compose.rememberAsyncImagePainter
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.authentication.AuthenticationViewModel
import org.mattshoe.shoebox.listery.authentication.model.SessionState
import org.mattshoe.shoebox.listery.logging.logi
import org.mattshoe.shoebox.listery.navigation.LocalNavController

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Level1AppBar(
    title: String,
    onProfileClick: () -> Unit = {},
    onMoreOptionsClick: () -> Unit = {}
) {
    val viewModel: AuthenticationViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val photoUrl by remember {
        derivedStateOf {
            (state as? SessionState.LoggedIn)?.user?.photoUrl
        }
    }
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
            IconButton(onClick = onProfileClick) {
                val url = photoUrl
                if (url != null) {
                    RemoteImage(
                        url = url,
                        contentDescription = "User Profile",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_account),
                        contentDescription = "User Profile",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            IconButton(onClick = onMoreOptionsClick) {
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

@Composable
fun RemoteImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    logi("Recomposing image! -- $url")
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .diskCacheKey(url)
            .memoryCacheKey(MemoryCache.Key(url))
            .crossfade(true)
            .build(),
        contentScale = contentScale
    )
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}