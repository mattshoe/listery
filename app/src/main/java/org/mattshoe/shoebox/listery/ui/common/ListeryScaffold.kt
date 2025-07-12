package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.bottomnav.ListeryNavigationBar
import org.mattshoe.shoebox.listery.ui.BottomNavItem

@Composable
fun ListeryScaffold(
    topBar: @Composable () -> Unit = {},
    selectedNavItem: BottomNavItem,
    showFab: Boolean = true,
    onFabClick: (() -> Unit)? = null,
    content: @Composable (padding: PaddingValues) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { topBar() },
        bottomBar = { ListeryNavigationBar(selectedNavItem, navController) },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = {
                        onFabClick?.invoke()
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { padding ->
        content(padding)
    }
}
