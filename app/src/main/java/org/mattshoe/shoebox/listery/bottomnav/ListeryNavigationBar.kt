package org.mattshoe.shoebox.listery.bottomnav

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.mattshoe.shoebox.listery.ui.BottomNavItem

@Composable
fun ListeryNavigationBar(
    selectedItem: BottomNavItem,
    navController: NavController
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        BottomNavItem.values.forEach {
            NavigationIcon(
                icon = it.icon,
                label = it.label,
                selected = it == selectedItem
            )
        }
    }
}

@Composable
fun RowScope.NavigationIcon(
    @DrawableRes icon: Int,
    label: String,
    selected: Boolean,
    onclick: () -> Unit = { }
) {
    NavigationBarItem(
        selected = selected,
        onClick = onclick,
        icon = {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        },
        label = {
            Text(text = label, color = MaterialTheme.colorScheme.onPrimary)
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.secondary
        )
    )
}