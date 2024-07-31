package org.mattshoe.shoebox.listery.cookbook.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.R

@Preview(showBackground = true)
@Composable
fun CookBookScreenPreview() {
    CookBookScreen()
}

@Composable
fun CookBookScreen() {
    Scaffold(
        topBar = { CookBookTopBar() },
        bottomBar = { ListeryNavigationBar() }
    ) { padding ->

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CookBookTopBar() {
    TopAppBar(
        title = {
            Text(text = "Cookbook")
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

@Composable
fun ListeryNavigationBar() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        NavigationIcon(icon = R.drawable.ic_meal_plan, label = "Plan")
        NavigationIcon(icon = R.drawable.ic_cookbook, label = "Cookbook")
        NavigationIcon(icon = R.drawable.ic_shopping_list, label = "Shopping")
    }
}

@Composable
fun RowScope.NavigationIcon(
    @DrawableRes icon: Int,
    label: String
) {
    NavigationBarItem(
        selected = false,
        onClick = { /*TODO*/ },
        icon = {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        },
        label = {
            Text(text = label, color = MaterialTheme.colorScheme.onPrimary)
        }
    )
}